public class Account extends Server{
    private String username;
    private String password;
    private double balance;


    public String createAccount(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            return "Please enter something in both fields!";
        }
        //To Do check if account exists
        else{
            createUser(username, password);
            return "Account Created";
        }
    }
}
