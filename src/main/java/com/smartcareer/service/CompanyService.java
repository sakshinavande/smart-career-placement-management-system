package com.smartcareer.service;

import com.smartcareer.entity.Company;
import com.smartcareer.repository.CompanyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // =========================================================
    // SAVE COMPANY
    // =========================================================

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    // =========================================================
    // GET ALL COMPANIES
    // =========================================================

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // =========================================================
    // GET COMPANY BY ID
    // =========================================================

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // =========================================================
    // GET COMPANY BY USER ID
    // =========================================================

    public Optional<Company> getCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    // =========================================================
    // UPDATE COMPANY
    // =========================================================

    public Company updateCompany(Long id, Company updatedCompany) {

        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found with ID: " + id));

        /*
         * Copy the company fields from updatedCompany
         * to existingCompany.
         *
         * ID and User are ignored because they should
         * not be changed during profile editing.
         */
        BeanUtils.copyProperties(
                updatedCompany,
                existingCompany,
                "id",
                "user"
        );

        return companyRepository.save(existingCompany);
    }

    // =========================================================
    // DELETE COMPANY
    // =========================================================

    public void deleteCompany(Long id) {

        if (!companyRepository.existsById(id)) {
            throw new RuntimeException(
                    "Company not found with ID: " + id
            );
        }

        companyRepository.deleteById(id);
    }
}