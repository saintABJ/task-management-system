package com.example.tasks_mgt_sys.entities.user.service;

import com.example.tasks_mgt_sys.entities.otp.dto.OtpRequest;
import com.example.tasks_mgt_sys.entities.otp.OtpType;
import com.example.tasks_mgt_sys.entities.otp.service.OtpService;
import com.example.tasks_mgt_sys.entities.user.dto.AuthResponse;
import com.example.tasks_mgt_sys.entities.user.dto.UserRequestDto;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;
import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import com.example.tasks_mgt_sys.entities.user.User;
import com.example.tasks_mgt_sys.entities.user.UserRepository;
import com.example.tasks_mgt_sys.exceptions.ConflictException;
import com.example.tasks_mgt_sys.exceptions.EntityNotFoundException;
import com.example.tasks_mgt_sys.entities.org.Organization;
import com.example.tasks_mgt_sys.entities.org.OrgRepository;
import com.example.tasks_mgt_sys.entities.role.RoleRepository;
import com.example.tasks_mgt_sys.entities.role.Role;
import com.example.tasks_mgt_sys.entities.role.RoleName;
import com.example.tasks_mgt_sys.services.EmailService;
import com.example.tasks_mgt_sys.services.JwtService;
import com.example.tasks_mgt_sys.utils.Helper;
import com.example.tasks_mgt_sys.utils.TokenPair;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrgRepository orgRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpService otpService;



    public UserServiceImpl(UserRepository userRepository, OrgRepository orgRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtService jwtService, AuthenticationManager authenticationManager, CurrentUserUtil currentUserUtil, EmailService emailService, OtpService otpService) {
        this.userRepository = userRepository;
        this.orgRepository = orgRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @Override
    @Transactional
    public UserResponseDto createUser(Long orgId, UserRequestDto userRequestDto) {

        //fetch Organization from db first
        /*Organization org = orgRepository.findById(userRequestDto.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Organization Not Found"));*/

        // Check if email exist for the organization
        /*if (userRepository.existsUserByEmailAndOrganizationId(userRequestDto.getEmail(), org.getId())) {
            throw new ConflictException("User with the email " + userRequestDto.getEmail() + " already exists in " + org.getName());
        }*/

        Organization organization = null;

        if (orgId != null) {
            organization = orgRepository.findById(orgId).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        }

        Optional<User> userCheck = userRepository.findByEmailIgnoreCase(userRequestDto.getEmail());
        if (userCheck.isPresent()) {
            throw new ConflictException("Email already exists");
        }

        Role role = roleRepository.findRoleByRole(organization != null ? RoleName.MEMBER : RoleName.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));


        User user = User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .organization(organization)
                .role(Set.of(role))
                .build();

        String otp = Helper.generateNumericOtp(6);

        otpService.generateAndStoreOtp(OtpRequest.builder()
                .otp(otp)
                .purpose(OtpType.ACCOUNT_VERIFICATION.name())
                .email(userRequestDto.getEmail())
                .build());

        User savedUser = userRepository.save(user);

        try {
            emailService.sendMail(
                    userRequestDto.getEmail(),
                    OtpType.ACCOUNT_VERIFICATION.name().replaceAll("_", " "),
                    "otp-email",
                    Map.of("otp", otp)
            );
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }


        //assert organization != null;
        return UserResponseDto.builder()

                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .organizationId(organization != null ? organization.getId() : null)
                .organizationName(organization != null ? organization.getName() : null)
                .build();

    }

    @Transactional
    @Override
    public void addRole(UUID userID, RoleName role) {

        User userToAdd = userRepository.findUserById(userID).orElseThrow(()
                -> new EntityNotFoundException("User not found"));

        Role addRole = roleRepository.findRoleByRole(role)
                .orElseThrow(() -> new EntityNotFoundException("Role access is invalid"));

        Set<Role> userCurrentRoles = userToAdd.getRole();

        if (userCurrentRoles.contains(addRole)) {
            throw new AccessDeniedException("User already has role");
        }
        userCurrentRoles.add(addRole);

        userToAdd.setRole(userCurrentRoles);
    }

    @Override
    public AuthResponse loginUser(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenPair tokenPair = jwtService.generateTokenPair(authentication);
        User user = userRepository.findByEmailIgnoreCase(userRequestDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return AuthResponse.builder()
                .tokenPair(tokenPair)
                .user(UserResponseDto.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                        .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                        .build())
                .build();
    }

}
