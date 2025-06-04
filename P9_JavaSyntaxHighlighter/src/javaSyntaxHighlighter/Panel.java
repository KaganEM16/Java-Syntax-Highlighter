package javaSyntaxHighlighter;

import java.util.List;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class Panel extends JFrame implements ActionListener, DocumentListener
{
	
	JLabel label;
    JTextPane textPane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(textPane);
    JButton[] buttons;
    
    public enum TokenType {
        KEYWORD(Color.BLUE, "\\b(int|float|double|char|String|boolean|void|class|interface|if|else|for|while|do|switch|case|break|continue|return|new|this|super|extends|implements|static|final|public|private|protected|import|package)\\b"),
        STRING(Color.GREEN, "\".*\""),
        NUMBER(Color.ORANGE, "\\b\\d+\\b"),
        COMMENT(Color.GRAY, "//.*|/\\*.*?\\*/"),
        OPERATOR(Color.RED, "[+\\-*/%=&|<>!^~?:]");
        
        final Color color;
        final String pattern;
        
        TokenType(Color color, String pattern) {
            this.color = color;
            this.pattern = pattern;
        }
    }

    public Panel() {
        super("Java Syntax Highlighter");
        int en = 770, boy = 770;
        this.setSize(en, boy);
        this.getContentPane().setBackground(Color.BLACK);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        
        label = new JLabel("Java Dilinde Bir Kod Prototipi Yazınız");
        setLabel(label, Color.LIGHT_GRAY, 25);
        label.setBounds(80, 20, 600, 40);
        this.add(label);
        
        textPane.setBackground(Color.LIGHT_GRAY);
        textPane.setFont(new Font("Verdana", Font.PLAIN, 18));
        scrollPane.setBounds(130, 100, 500, 500);
        this.add(scrollPane);
        textPane.getDocument().addDocumentListener(this);
        
        buttons = new JButton[2];
        for(int i=0 ; i<buttons.length ; i++) {
        	buttons[i] = (i==0)? new JButton("Kodu Sil") : new JButton("Kodu Derle");
        	buttons[i].setBackground(Color.LIGHT_GRAY);
        	buttons[i].setForeground(Color.BLUE);
        	buttons[i].setFont(new Font("Verdana",Font.BOLD ,16));
        	buttons[i].addActionListener(this);
        	this.add(buttons[i]);
        }
        buttons[0].setBounds(200, 630, 150, 60);
        buttons[1].setBounds(400, 630, 150, 60);
        
        this.setVisible(true);
    }
    
    public void setLabel(JLabel label, Color renk, int boyut)
	{
		label.setForeground(renk);
		label.setFont(new Font("Verdana",Font.BOLD ,boyut));
		label.setHorizontalAlignment(0);
	}
    
    public void highlight() {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc = textPane.getStyledDocument();
            String text;
            
            try {
                text = doc.getText(0, doc.getLength());
            } catch (BadLocationException e) {
                return;
            }

            // Önce tüm yazıyı varsayılan renge döndür
            Style defaultStyle = textPane.addStyle("default", null);
            StyleConstants.setForeground(defaultStyle, Color.BLACK);
            doc.setCharacterAttributes(0, text.length(), defaultStyle, true);

            // Her token tipi için vurgulama yap
            for (TokenType type : TokenType.values()) {
                Pattern pattern = Pattern.compile(type.pattern);
                Matcher matcher = pattern.matcher(text);
                
                while (matcher.find()) {
                    Style style = textPane.addStyle(type.name(), null);
                    StyleConstants.setForeground(style, type.color);
                    doc.setCharacterAttributes(matcher.start(), 
                                              matcher.end() - matcher.start(), 
                                              style, true);
                }
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JButton basilanButon = (JButton)e.getSource();
		
		if(basilanButon == buttons[0]) {
			textPane.setText("");
		}else {		    
		    
			String kod = textPane.getText().trim();
			if (kod.isEmpty()) {
			    JOptionPane.showMessageDialog(this, "Kod alanı boş.", "Uyarı", JOptionPane.WARNING_MESSAGE);
			    return;
			}
			try {
			    Lexer lexer = new Lexer(kod);
			    List<Token> tokens = lexer.tokenize();
			    Parser parser = new Parser(tokens);
			    parser.parse();

			    // Parsing başarılı ise JOptionPane göster
			    SwingUtilities.invokeLater(() -> {
			        JOptionPane.showMessageDialog(null, "Kod geçerli!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
			    });

			} catch (RuntimeException ex) {
			    SwingUtilities.invokeLater(() -> {
			        JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
			    });
			}
		    
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) 
	{
		highlight();
	}

	@Override
	public void removeUpdate(DocumentEvent e) 
	{
		highlight();
	}

	@Override
	public void changedUpdate(DocumentEvent e) 
	{
		// Bu Metot proje için gereksiz bir metottur.
	}
}


