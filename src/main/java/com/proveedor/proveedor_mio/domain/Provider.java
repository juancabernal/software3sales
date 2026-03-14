package com.proveedor.proveedor_mio.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("providers")
public class Provider {

    @Id
    private String id;
    private String businessName;
    private String documentTypeId;
    private String documentNumber;
    private String taxRegimeId;
    private String responsibleFirstName;
    private String responsibleLastName;
    private String phone;
    private String email;
    private String departmentId;
    private String cityId;
    private String address;
    private String branchId;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(String taxRegimeId) {
        this.taxRegimeId = taxRegimeId;
    }

    public String getResponsibleFirstName() {
        return responsibleFirstName;
    }

    public void setResponsibleFirstName(String responsibleFirstName) {
        this.responsibleFirstName = responsibleFirstName;
    }

    public String getResponsibleLastName() {
        return responsibleLastName;
    }

    public void setResponsibleLastName(String responsibleLastName) {
        this.responsibleLastName = responsibleLastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
