package pl.sdacademy.models;

import pl.sdacademy.controllers.AccountantController;
import pl.sdacademy.exceptions.AccountantAlreadyExistException;
import pl.sdacademy.exceptions.AccountantNotFoundException;
import pl.sdacademy.exceptions.AccountantPasswordIsToShort;
import pl.sdacademy.exceptions.AccountantWrongLogin;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class AccountantRegistry extends Accountant implements Serializable {
    private static AccountantRegistry instance = null;
    private Set<Accountant> accountants;


    public AccountantRegistry() {
        this.accountants = new HashSet<>();
    }

    public static AccountantRegistry getInstance()  {
        if (instance == null) {
            instance = new AccountantRegistry();
        }
        return instance;
    }


    public static void saveAccountantToFile(Set<Accountant> accountants) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/resources/myAccountantRegistry.bin"));
        objectOutputStream.writeObject(accountants);
        objectOutputStream.close();
    }

    public static void readAccountantsFromFile(Set<Accountant> accountants) throws IOException, ClassNotFoundException, AccountantPasswordIsToShort, AccountantAlreadyExistException, AccountantWrongLogin {

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/main/resources/myAccountantRegistry.bin"));
        Set<Accountant> list = (Set<Accountant>) objectInputStream.readObject();
        getInstance().removeAllAccountants();
        for (Accountant a : list) {
            getInstance().addAccountant(a);
        }
        objectInputStream.close();
    }

    private void removeAllAccountants(){
        accountants = new HashSet<>();
    }

    public void addAccountant(Accountant accountant) throws AccountantAlreadyExistException, AccountantPasswordIsToShort, AccountantWrongLogin, IOException, ClassNotFoundException {

            if (accountant.getLogin().trim().length() < 1) {
                throw new AccountantWrongLogin("Nie moze byc pusty login! ");

            }
            if (accountants.contains(accountant)) {
                throw new AccountantAlreadyExistException("podany login jest zajety");
            } else {
                if (accountant.getPassword().trim().length() < 3) {
                    throw new AccountantPasswordIsToShort("Podane haslo jest za krotkie, musi sie skladac z przynajmniej 3 znakow");
                } else {
                    String login = accountant.getLogin().split(" ")[ 0 ];
                    String password = accountant.getPassword();
                    accountant = new Accountant(login, password);
                    accountants.add(accountant);
                    AccountantController.saveAccountant();
                }
            }
        }

    public void removeAccountant(String login) throws AccountantNotFoundException, IOException, ClassNotFoundException {
        boolean removed = false;

        for (Accountant accountant : accountants) {
            if (accountant.getLogin().equals(login)) {
                accountants.remove(accountant);
                AccountantController.saveAccountant();
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new AccountantNotFoundException("Nie ma takiego ksiegowego");
        }
    }

    public Accountant findAccountant(String login, String password) throws AccountantNotFoundException {
        for (Accountant accountant : accountants) {
            if (accountant.getLogin().equals(login) && accountant.getPassword().equals(password)) {
                return accountant;
            }
        }
        throw new AccountantNotFoundException("Zły login lub hasło");

    }

    public Set<Accountant> getAccountants() {
        return accountants;
    }


    public Accountant findAccountantByLogin(String login) throws AccountantNotFoundException {

        for (Accountant accountant : accountants) {
            if (accountant.getLogin().equals(login)) {
                return accountant;
            }
        }

        throw new AccountantNotFoundException("Nie ma takiego ksiegowego");
    }

}