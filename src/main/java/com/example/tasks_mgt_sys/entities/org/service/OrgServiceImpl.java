package com.example.tasks_mgt_sys.entities.org.service;

import com.example.tasks_mgt_sys.entities.Audit.Audit;
import com.example.tasks_mgt_sys.entities.org.dto.OrgRequestDto;
import com.example.tasks_mgt_sys.entities.org.dto.OrgResponseDto;
import com.example.tasks_mgt_sys.entities.org.OrgRepository;
import com.example.tasks_mgt_sys.entities.org.Organization;
import com.example.tasks_mgt_sys.entities.user.dto.UserResponseDto;
import com.example.tasks_mgt_sys.exceptions.ConflictException;
import com.example.tasks_mgt_sys.entities.user.UserRepository;
import com.example.tasks_mgt_sys.entities.user.CurrentUserUtil;
import com.example.tasks_mgt_sys.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrgServiceImpl implements OrgService {

    private final OrgRepository orgRepository;
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;

    public OrgServiceImpl(OrgRepository orgRepository, CurrentUserUtil currentUserUtil, UserRepository userRepository) {
        this.orgRepository = orgRepository;
        this.currentUserUtil = currentUserUtil;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @Audit(action = "CREATE", entity = "Organization")
    public OrgResponseDto createOrg(OrgRequestDto orgRequestDto) {

        User user = currentUserUtil.getLoggedInUser();

        if (user.getOrganization() != null)
            throw new ConflictException("User already belong to an organization");

        Organization organization = Organization.builder()
                .name(orgRequestDto.getName())
                .createdAt(LocalDateTime.now())
                .build();

        user.setOrganization(organization);
        organization.setUser(List.of(user));
        Organization savedOrg = orgRepository.save(organization);

        return OrgResponseDto.builder()
                .orgName(savedOrg.getName())
                .createdDate(savedOrg.getCreatedAt())
                .build();
    }

    @Override
    public List<UserResponseDto> viewOrgUsers() {

        User user = currentUserUtil.getLoggedInUser();

        return userRepository.findUserByOrganization(user.getOrganization())
                .stream().map(u -> UserResponseDto.builder()
                        .userId(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .organizationId(u.getOrganization().getId())
                        .organizationName(u.getOrganization().getName())
                        .build()).toList();
    }
}
