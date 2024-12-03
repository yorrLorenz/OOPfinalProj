package Components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GradientPaint;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;
import Classes.Data;
import Helpers.CredentialChecker;
import Helpers.ErrorHandler;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterForm extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel register;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JFormattedTextField usernameText;
    private JLabel usernameLabel;
    private JLabel returnLabel;
    private JButton registerButton;
    
    private String color1 = "#FFFFFF";
    private String color2 = "#FFFFFF";
    
    /**
     * Create the panel.
     */
    public RegisterForm() {
        setBackground(Color.WHITE);
        setLayout(null);
        setPreferredSize(new Dimension(450, getToolkit().getScreenSize().height));
        
        register = new JLabel("Register");
        register.setBounds(78, 158, 185, 63);
        register.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 54));
        add(register);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(78, 388, 283, 34);
        add(passwordField);
        
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(78, 351, 81, 24);
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(passwordLabel);
        
        usernameText = new JFormattedTextField();
        usernameText.setBounds(78, 306, 283, 34);
        usernameText.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        add(usernameText);
        
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(78, 271, 84, 24);
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(usernameLabel);
        
        returnLabel = new JLabel("return to login");
        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                returnLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                returnLabel.setForeground(Color.BLACK);
            }
        });
        returnLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                returnLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                returnLabel.setForeground(Color.BLUE);
            }
        });
        returnLabel.setBounds(78, 436, 128, 17);
        returnLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        add(returnLabel);
        
        registerButton = new JButton("Register") {
            protected void paintComponent(Graphics g) {
                int w = registerButton.getWidth();
                int h = registerButton.getHeight();
                Graphics2D g2 = (Graphics2D)g;
                GradientPaint gra = new GradientPaint(0,0, Color.decode(color1), w/4,0, Color.decode(color2));
                g2.setPaint(gra);
                g2.fillRoundRect(0, 0, w, h, h, h);
                super.paintComponent(g);
            }
        };
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread.startVirtualThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        String username = usernameText.getText();
                        String password = String.valueOf(passwordField.getPassword());
                        if(CredentialChecker.checkUsername(username) && CredentialChecker.checkPassword(password)) {
                            registerAccount();
                        } else if(!CredentialChecker.checkUsername(username)) {
                            new ErrorHandler("\nUsername must have:\na length of 4-25 characters;\nno whitespaces;", getParent());
                        } else if(!CredentialChecker.checkPassword(password)) {
                            new ErrorHandler("\nPassword must contain:\nat least one lowercase letter;\nat least one uppercase letter;\nat least one digit;\nat least 8 characters long;", getParent());
                        } else {
                            new ErrorHandler("Please enter valid credentials", getParent());
                        }
                    }
                });
            }

            private void registerAccount() {
                JProgressBar bar = new JProgressBar(0, 100); 
                bar.setStringPainted(true);
                bar.setBounds(getWidth()/6, getHeight()/2, 300,30);
                bar.setForeground(Color.GREEN);
                add(bar);
                repaint();
                revalidate();
                String user = usernameText.getText();
                String pass = String.valueOf(passwordField.getPassword());
                bar.setValue(10);
                addAccount(user, pass, bar);
                JSONArray arr = Data.getData(new File("src/Data/users.json"), getParent());
                JOptionPane.showMessageDialog(getParent(), "Account added: " + arr.getJSONObject(arr.length() - 1).getString("username"), "Register", JOptionPane.INFORMATION_MESSAGE);
                bar.setValue(100);
                remove(bar);
                repaint();
                revalidate();
            }

            private void addAccount(String user, String pass, JProgressBar bar) {
                // Add user to users.json
                File usersFile = new File("src/Data/users.json");
                bar.setValue(15);
                JSONArray usersArray = Data.getData(usersFile, getParent());
                bar.setValue(45);
                
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(100);
                        bar.setValue(bar.getValue() + 4);
                    }
                } catch (InterruptedException e) {
                    new ErrorHandler("Error in thread; " + e.getMessage(), getParent());
                }
                
                // Create new user object for users.json
                JSONObject userObj = new JSONObject();
                userObj.put("password", pass);
                userObj.put("username", user);
                usersArray.put(userObj);
                
                try (FileWriter write = new FileWriter(usersFile)) {
                    usersArray.write(write);
                    bar.setValue(90);
                    write.flush();
                } catch (IOException e) {
                    new ErrorHandler("Error writing username and password to users.json", getParent());
                }

                // Add bank account to bankaccounts.json
                File bankAccountsFile = new File("src/Data/bankaccounts.json");
                JSONArray bankAccountsArray;

                if (bankAccountsFile.exists()) {
                    bankAccountsArray = new JSONArray(Data.getData(bankAccountsFile, getParent()).toString());
                } else {
                    bankAccountsArray = new JSONArray();
                }

                JSONObject bankAccountObj = new JSONObject();
                bankAccountObj.put("username", user); // Link to the user
                bankAccountObj.put("balance", 0.0);  // Initial balance is 0
                bankAccountObj.put("transactions", new JSONArray()); // Empty transactions array

                // Add the bank account to the array
                bankAccountsArray.put(bankAccountObj);

                // Write the updated bank accounts data back to the file
                try (FileWriter bankWriter = new FileWriter(bankAccountsFile)) {
                    bankAccountsArray.write(bankWriter);
                    bankWriter.flush();
                } catch (IOException e) {
                    new ErrorHandler("Error writing bank account data to bankaccounts.json", getParent());
                }
            }
        });
        
        registerButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                registerButton.setForeground(Color.WHITE);
                color1 = "#892643"; 
                color2 = "#e98faa";
                registerButton.repaint();
                registerButton.revalidate();
            }
        });
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setForeground(Color.BLACK);
                color1 = "#FFFFFF";
                color2 = "#FFFFFF"; 
                registerButton.repaint();
                registerButton.revalidate();
            }
        });
        registerButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(null);
        registerButton.setBounds(255, 494, 106, 40);
        add(registerButton);
    }

    public JLabel getRegister() {
        return register;
    }

    public void setRegister(JLabel register) {
        this.register = register;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public void setPasswordLabel(JLabel passwordLabel) {
        this.passwordLabel = passwordLabel;
    }

    public JFormattedTextField getUsernameText() {
        return usernameText;
    }

    public void setUsernameText(JFormattedTextField usernameText) {
        this.usernameText = usernameText;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public void setUsernameLabel(JLabel usernameLabel) {
        this.usernameLabel = usernameLabel;
    }

    public JLabel getReturnLabel() {
        return returnLabel;
    }

    public void setReturnLabel(JLabel returnLabel) {
        this.returnLabel = returnLabel;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(JButton registerButton) {
        this.registerButton = registerButton;
    }
}
