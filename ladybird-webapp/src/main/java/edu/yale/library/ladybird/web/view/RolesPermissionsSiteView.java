package edu.yale.library.ladybird.web.view;

import edu.yale.library.ladybird.entity.Permissions;
import edu.yale.library.ladybird.entity.Roles;
import edu.yale.library.ladybird.entity.RolesPermissions;
import edu.yale.library.ladybird.persistence.dao.PermissionsDAO;
import edu.yale.library.ladybird.persistence.dao.RolesDAO;
import edu.yale.library.ladybird.persistence.dao.RolesPermissionsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.List;

//TODO rename
@ManagedBean
@RequestScoped
public class RolesPermissionsSiteView extends AbstractView {

    private Logger logger = LoggerFactory.getLogger(RolesPermissionsSiteView.class);

    @Inject
    PermissionsDAO permissionsDAO;

    @Inject
    RolesDAO rolesDAO;

    @Inject
    RolesPermissionsDAO rolesPermissionsDAO;

    private boolean enabled = false;

    private Roles roles = new Roles();

    private List<Roles> rolesList;

    private Permissions permissions = new Permissions();

    private List<Permissions> permissionsList;

    private List<RolesPermissions> itemList;

    @PostConstruct
    public void init() {
        initFields();
        rolesList = rolesDAO.findAll();
        permissionsList = permissionsDAO.findAll();
        itemList = rolesPermissionsDAO.findAll();
    }

    public String save() {
        logger.debug("Updating permssion={} for role={} with value={}",
                permissions.getPermissionsId(), roles.getRoleId(), enabled);
        try {
            RolesPermissions rolesPermissions = rolesPermissionsDAO.findByRolesPermissionsId(roles.getRoleId(), permissions.getPermissionsId());

            logger.debug("Entity={}", rolesPermissions);

            if (enabled) {
                rolesPermissions.setValue('y');
            } else {
                rolesPermissions.setValue('n');
            }

            rolesPermissionsDAO.updateItem(rolesPermissions);
            logger.debug("New entity={}", rolesPermissionsDAO.findByRolesPermissionsId(roles.getRoleId(), permissions.getPermissionsId()));
            return ok();
        } catch (Exception e) {
            logger.error("Error updating roles permissions pair", e);
            return fail();
        }
    }

    public String getRoleName(int roleId) {
        return rolesDAO.findById(roleId).getRoleName();

    }

    public String getPermissionsName(int permissionsId) {
        return permissionsDAO.findById(permissionsId).getPermissionsName();
    }

    //getters and setters ---------------------------------------------------------------

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public List<Roles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        this.rolesList = rolesList;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<RolesPermissions> getItemList() {
        return itemList;
    }

    public void setItemList(List<RolesPermissions> itemList) {
        this.itemList = itemList;
    }

}
