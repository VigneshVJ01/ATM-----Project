import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    /**
     * The first name of the user
     */
    private String firstName;

    /**
     * The last name of the user
     */
    private String lastName;

    /**
     * The uuid number of the user
     */
    private String uuid;

    /**
     * The MD5 hash of the user's pin number
     */
    private byte pinHash[];

    /**
     * The list of accounts for this user
     */
    private ArrayList<Account> accounts;

    /**
     * 
     * @param firstName
     * @param lastName
     * @param pin
     * @param theBank
     */
    public User(String firstName, String lastName, String pin, Bank theBank)
    {
        // set user's name
        this.firstName = firstName;
        this.lastName =lastName;

        //storethe pin's MD5 hash, rather than the orginal value, for security reasons
        try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e){
            System.out.println("Error , caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // get a new universal ID for the user
        this.uuid = theBank.getNewUserUUID();

        //create empty list of accounts
        this.accounts = new ArrayList<Account>();

        // print log message
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName,this.uuid);
    }

    /**
     * Add an account for the user
     * @param anAcct the account to add
     */
    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }
/**
 * Return the user's UUID
 * @return the uuid
 */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check whether a given pin matches the true User pin
     * @param aPin the pin to check
     * @return whether the pin is validate or not
     */
    public boolean validatePin(String aPin) {
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()),
                    this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error , caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        return false;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void printAccountSummary() {

        System.out.printf("\n\n%s's accounts summary:\n ", this.firstName);
        for(int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("  %d) %s\n", a+1,
                    this.accounts.get(a).getSummarLine());
        }
        System.out.println();
    }

    /**
     * Get the number of accounts of the year
     * @return the number of accounts
     */
    public int numAccounts() {
        return this.accounts.size();
    }

    /**
     * Print transaction history for a particular account
     * @param accIdx the index of the account to use
     */
    public void printAcctTransHistory(int accIdx) {
        this.accounts.get(accIdx).printTransHistory();
    }

    /**
     * get the balance of a particular account
     * @param acctIdx the index of the account to use
     * @return the balance of the account
     */
    public double getAcctBalance(int acctIdx){
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * Get the UUID of a particular acount
     * @param acctIdx the index of the account to use
     * @return the UUID of the account
     */
    public String getAcctUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }
    
    public void addAcctTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
