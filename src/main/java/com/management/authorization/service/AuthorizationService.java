package com.management.authorization.service;

import com.management.company.model.Company;
import com.management.company.model.CompanyMember;
import com.management.company.repository.CompanyMemberRepository;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyMemberRepository companyMemberRepository;

    public boolean hasFinancialAccess(Integer personId, Integer projectId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            return false;
        }
        Project project = projectOptional.get();

        Company company = project.getCompany();
        if (company == null) {
            return false;
        }

        Optional<CompanyMember> companyMemberOptional = companyMemberRepository.findByCompanyIdAndPersonId(company.getId(), personId);
        if (companyMemberOptional.isEmpty()) {
            return false;
        }
        CompanyMember companyMember = companyMemberOptional.get();

        Role role = companyMember.getRole();
        if (role == null) {
            return false;
        }

        String roleName = role.getName();
        return "Owner".equals(roleName) || "Manager".equals(roleName);
    }
}
