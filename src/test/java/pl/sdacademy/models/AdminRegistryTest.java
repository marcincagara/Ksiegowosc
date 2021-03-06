package pl.sdacademy.models;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.sdacademy.exceptions.AdminNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.rmi.registry.Registry;

import static org.junit.Assert.*;

public class AdminRegistryTest {
    @Test
    public void shouldAddAdmin() throws IOException {
        Admin created = new Admin("Anna", "321");
        AdminRegistry.getInstance().addAdmin(created);

        Admin result = null;
        try {
            result = AdminRegistry.getInstance().findAdmin("Anna", "321");
        } catch (AdminNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(created, result);
    }

    @Test(expected = AdminNotFoundException.class)
    public void shouldRemoveAdmin() throws AdminNotFoundException, IOException {
        Admin created = new Admin("Anna", "321");
        AdminRegistry.getInstance().addAdmin(created);

        AdminRegistry.getInstance().remove("Anna");
        AdminRegistry.getInstance().findAdmin("Anna", "321");
    }

    @Test(expected = AdminNotFoundException.class)
    public void shouldNotAddAdminBadLogin() throws AdminNotFoundException, IOException {
        Admin created = new Admin("  ", "321");
        AdminRegistry.getInstance().addAdmin(created);
        Admin result = AdminRegistry.getInstance().findAdmin("  ", "321");
    }

    @Test(expected = AdminNotFoundException.class)
    public void shouldNotAddAdminBadPassword() throws AdminNotFoundException, IOException {
        Admin created = new Admin("Anna", " 12");
        AdminRegistry.getInstance().addAdmin(created);
        Admin result = AdminRegistry.getInstance().findAdmin("Anna", " 12");
    }
    @Test
    public void shouldNotAddCopyAdmin() throws IOException {
        Admin created = new Admin("Anna", "123");
        AdminRegistry.getInstance().addAdmin(created);

        Admin result = null;
        try {
         AdminRegistry.getInstance().addAdmin(created);
         AdminRegistry.getInstance().findAdmin("Anna", "123");
        } catch (AdminNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertNotEquals(created, result);
    }


}