/*******************************************************************************
 * ===========================================================
 * Ankush : Big Data Cluster Management Solution
 * ===========================================================
 * 
 * (C) Copyright 2014, by Impetus Technologies
 * 
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation;
 * 
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.impetus.ankush.common.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * This class represents the basic "user" object in AppFuse that allows for authentication and user management. It
 * implements Acegi Security's UserDetails interface.
 *
 */
@Entity
@Table(name = "ankush_user")
@NamedQuery(name="getUserByRole", query="select u FROM User u WHERE :role MEMBER OF u.roles")
@XmlRootElement
@JsonIgnoreProperties(value={"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "forcePasswordChange"}, ignoreUnknown=true)
public class User extends BaseObject implements UserDetails {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3832626162173359411L;

    /** The id. */
    private Long id;
    
    /** The username. */
    private String username; // required
    
    /** The password. */
    private String password; // required
    
    /** The first name. */
    private String firstName; // required
    
    /** The last name. */
    private String lastName; // required
    
    /** The email. */
    private String email; // required; unique
    
    /** The mobile. */
    private String mobile;
    
    /** The version. */
    private Integer version;
    
    /** The roles. */
    private Set<Role> roles = new HashSet<Role>();
    
    /** The enabled. */
    private boolean enabled;
    
    /** The account expired. */
    private boolean accountExpired;
    
    /** The account locked. */
    private boolean accountLocked;
    
    /** The credentials expired. */
    private boolean credentialsExpired;
    
    /** The creation date. */
    private Date creationDate;
    
    /** The last login. */
    private Date lastLogin;
    
    /** The force password change. */
    private Boolean forcePasswordChange;
    
    /**
     * Gets the force password change.
     *
     * @return the force password change
     */
    public Boolean getForcePasswordChange() {
		return forcePasswordChange;
	}

	/**
	 * Sets the force password change.
	 *
	 * @param forcePasswordChange the new force password change
	 */
	public void setForcePasswordChange(Boolean forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	/** The user status. */
	private String userStatus="None";

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public User() {
    }

    /**
     * Create a new instance and set the username.
     *
     * @param username
     *            login name for user.
     */
    public User(final String username) {
        this.username = username;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Column(nullable = false, length = 50, unique = true)
    public String getUsername() {
        return username;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Column(nullable = false)
    @XmlTransient
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    @Column(name = "first_name", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    @Column(name = "last_name", nullable = false, length = 50)
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    @Column(nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    /**
     * Returns the full name.
     *
     * @return firstName + ' ' + lastName
     */
    @Transient
    @JsonIgnore
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    /**
     * Gets the roles.
     *
     * @return the roles
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Adds a role for the user.
     *
     * @param role the fully instantiated role
     */
    public void addRole(Role role) {
        getRoles().add(role);
    }

    /**
     * Gets the authorities.
     *
     * @return GrantedAuthority[] an array of roles.
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @JsonIgnore
    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        authorities.addAll(roles);
        return authorities;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    @Version
    @JsonIgnore
    public Integer getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Column(name = "account_enabled")
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Checks if is account expired.
     *
     * @return true, if is account expired
     */
    @JsonIgnore
    @Column(name = "account_expired", nullable = false)
    public boolean isAccountExpired() {
        return accountExpired;
    }

    /**
     * Checks if is account non expired.
     *
     * @return true if account is still active
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @JsonIgnore
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    /**
     * Checks if is account locked.
     *
     * @return true, if is account locked
     */
    @JsonIgnore
    @Column(name = "account_locked", nullable = false)
    public boolean isAccountLocked() {
        return accountLocked;
    }

    /**
     * Checks if is account non locked.
     *
     * @return false if account is locked
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @JsonIgnore
    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    /**
     * Checks if is credentials expired.
     *
     * @return true, if is credentials expired
     */
    @JsonIgnore
    @Column(name = "credentials_expired", nullable = false)
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * Checks if is credentials non expired.
     *
     * @return true if credentials haven't expired
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @JsonIgnore
    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the roles.
     *
     * @param roles the new roles
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Sets the version.
     *
     * @param version the new version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets the account expired.
     *
     * @param accountExpired the new account expired
     */
    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    /**
     * Sets the account locked.
     *
     * @param accountLocked the new account locked
     */
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    /**
     * Sets the credentials expired.
     *
     * @param credentialsExpired the new credentials expired
     */
    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }
    
    /**
     * Gets the mobile.
     *
     * @return the mobile
     */
    @JsonIgnore
    @Column(length=20)
    public String getMobile() {
		return mobile;
	}

	/**
	 * Sets the mobile.
	 *
	 * @param mobile the new mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	@JsonIgnore
	@Column(updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the last login.
	 *
	 * @return the last login
	 */
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * Sets the last login.
	 *
	 * @param lastLogin the new last login
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;

        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this.id, ToStringStyle.DEFAULT_STYLE).append("id", this.id)
        		.append("username", this.username).append("enabled", this.enabled).append("accountExpired", this.accountExpired)
                .append("credentialsExpired", this.credentialsExpired).append("accountLocked", this.accountLocked);

        if (roles != null) {
            sb.append("Granted Authorities: ");

            int i = 0;
            for (Role role : roles) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(role.toString());
                i++;
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString();
    }
    
    /**
     * Sets the creation time.
     */
    @PrePersist
    public void setCreationTime() {
        if (creationDate == null) {
            creationDate = new Date();
        }
    }
    
    /* (non-Javadoc)
     * @see com.impetus.ankush.common.domain.BaseObject#merge(com.impetus.ankush.common.domain.BaseObject)
     */
    @Override
    public void merge(BaseObject baseObject) {
    	User user = (User) baseObject;
    	if(StringUtils.isNotBlank(user.getMobile())){
    		this.setMobile(user.getMobile());
    	}
    	if(StringUtils.isNotBlank(user.getEmail())){
    		this.setEmail(user.getEmail());
    	}
    	if(StringUtils.isNotBlank(user.getPassword())){
    	    this.setPassword(user.getPassword());
    	}
    	if(StringUtils.isNotBlank(user.getFirstName())){
    		this.setFirstName(user.getFirstName());
    	}
    	if(StringUtils.isNotBlank(user.getLastName())){
    		this.setLastName(user.getLastName());
    	}
    	this.setEnabled(user.isEnabled());
    	super.merge(baseObject);
    }

	/**
	 * Gets the user status.
	 *
	 * @return the userStatus
	 */
    //@JsonIgnore
    @Transient
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * Sets the user status.
	 *
	 * @param userStatus the userStatus to set
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
}
