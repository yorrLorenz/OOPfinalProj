package Driver;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import Classes.Data;
import Classes.User;
import Components.ContainerPage;
import Components.FormPage;
import Components.LoginForm;
import Helpers.CredentialChecker;
import Helpers.ErrorHandler;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.awt.event.ActionEvent;

public class Window {

    private JFrame window;
    private FormPage formPage = null;
    private static ContainerPage containerPage = null;
    private static User user = null; 

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window window = new Window();
                    window.window.setVisible(true);
                } catch (Exception e) {
                    new ErrorHandler("window is not working", null);
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Window() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        window = new JFrame();
        window.getContentPane().setBackground(Color.WHITE);
        window.setResizable(false);
        window.setBounds(100, 100, 450, window.getToolkit().getScreenSize().height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.getContentPane().setLayout(null);
        formPage = new FormPage();
        containerPage = new ContainerPage();

        formPage.getLoginForm().getLoginButton().addActionListener(new ActionListener() {
            private JProgressBar bar = new JProgressBar(0, 100);

            public void actionPerformed(ActionEvent e) {
                Thread.startVirtualThread(new Runnable() {
                    @Override
                    public void run() {
                        bar.setStringPainted(true);
                        bar.setForeground(Color.GREEN);
                        bar.setBounds(formPage.getWidth() / 6, formPage.getHeight() / 2, 300, 30);
                        formPage.add(bar);
                        formPage.repaint();
                        formPage.revalidate();
                        try {
                            while (bar.getValue() < 100) {
                                Thread.sleep(100);
                                bar.setValue(bar.getValue() + new Random().nextInt(5, 15));
                            }
                        } catch (InterruptedException e) {
                            new ErrorHandler("error in thread.sleep\n" + e.getStackTrace(), window);
                        }

                        formPage.remove(bar);
                        formPage.repaint();
                        formPage.revalidate();
                        loginHandler();
                    }
                });
            }

            private void loginHandler() {
                LoginForm lForm = formPage.getLoginForm();
                String username = lForm.getUsernameText().getText();
                String password = String.valueOf(lForm.getPasswordField().getPassword());

                boolean isLogin = checkAccount(username, password);

                if (CredentialChecker.checkUsername(username) && CredentialChecker.checkPassword(password) && isLogin) {
                    window.getContentPane().remove(formPage);
                    window.getContentPane().repaint();
                    window.getContentPane().revalidate();

                    bar.setValue(0);
                    window.getContentPane().add(bar);
                    window.getContentPane().repaint();
                    window.getContentPane().revalidate();

                    try {
                        while (bar.getValue() < 100) {
                            Thread.sleep(100);
                            bar.setValue(bar.getValue() + new Random().nextInt(5, 15));
                        }
                    } catch (InterruptedException e) {
                        new ErrorHandler("error in thread.sleep\n" + e.getStackTrace(), window);
                    }

                    window.getContentPane().remove(bar);
                    window.getContentPane().repaint();
                    window.getContentPane().revalidate();

                    // Update the container page with the username
                    containerPage.updateUsername(username);

                    // Now load the data for the user
                    loadUserData(username);

                    window.getContentPane().add(containerPage);
                    window.getContentPane().repaint();
                    window.getContentPane().revalidate();
                } else if (!CredentialChecker.checkUsername(username)) {
                    new ErrorHandler("\nUsername must have:\na length of 4-25 characters;\nno whitespaces;", formPage);
                } else if (!CredentialChecker.checkPassword(password)) {
                    new ErrorHandler("\nPassword must contain:\nat least one lowercase letter;\nat least one uppercase letter;\nat least one digit;\nat least 8 characters long;", formPage);
                } else {
                    new ErrorHandler("Account not found.", formPage);
                }
            }

            private boolean checkAccount(String username, String password) {
                boolean isLogin = false;

                JSONArray users = Data.getData(new File("src/Data/users.json"), window);

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    if (username.contains(user.getString("username")) && password.contains(user.getString("password"))) {
                        isLogin = true;
                    }
                }
                return isLogin;
            }
        });

        containerPage.setBounds(0, 0, 450, containerPage.getToolkit().getScreenSize().height);
        formPage.setBounds(0, 0, 436, 683);
        window.getContentPane().add(formPage);
    }

    // New function to load the user's data (balance, transactions, expenses)
    private void loadUserData(String username) {
        JSONArray users = Data.getData(new File("src/Data/users.json"), window);

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                // Check if the "balance" key exists
                int balance = user.has("balance") ? user.getInt("balance") : 0;  // Default to 0 if missing
                JSONArray transactions = user.optJSONArray("transactions");  // Use optJSONArray to avoid exception if missing
                JSONArray expenses = user.optJSONArray("expenses");  // Use optJSONArray to avoid exception if missing

                // Now load the user's data into memory
                User currentUser = new User(username, balance, transactions, expenses);
                setUser(currentUser);  // Set the global user object

                // You can update the containerPage with more user-specific data if needed.
                // containerPage.updateUserData(currentUser);
            }
        }
    }


    public JFrame getFrame() {
        return window;
    }

    public void setFrame(JFrame frame) {
        this.window = frame;
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public FormPage getFormPage() {
        return formPage;
    }

    public void setFormPage(FormPage formPage) {
        this.formPage = formPage;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Window.user = user;
    }
}
