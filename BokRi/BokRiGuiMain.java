package bokri;

import javax.swing.*;
import java.awt.*;

public class BokRiGuiMain extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField principalField;
    private JTextField percentField;
    private JTextField daysField;
    private JTextArea resultArea;

    public BokRiGuiMain() {
        setTitle("복리 계산기 (KRW / 한글 / USDT)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // 화면 가운데

        initComponents();
    }

    private void initComponents() {
        // 상단 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel principalLabel = new JLabel("원금 (원): ");
        principalField = new JTextField();

        JLabel percentLabel = new JLabel("하루 수익률 (%): ");
        percentField = new JTextField();

        JLabel daysLabel = new JLabel("기간 (일): ");
        daysField = new JTextField();

        JButton calculateButton = new JButton("계산하기");

        inputPanel.add(principalLabel);
        inputPanel.add(principalField);
        inputPanel.add(percentLabel);
        inputPanel.add(percentField);
        inputPanel.add(daysLabel);
        inputPanel.add(daysField);
        inputPanel.add(new JLabel()); // 빈칸
        inputPanel.add(calculateButton);

        // 결과 영역
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 레이아웃 구성
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 이벤트
        calculateButton.addActionListener(e -> onCalculate());
    }

    private void onCalculate() {
        try {
            long principal = Long.parseLong(principalField.getText().replaceAll(",", "").trim());
            double percent = Double.parseDouble(percentField.getText().trim());
            int days = Integer.parseInt(daysField.getText().trim());

            if (principal <= 0 || days <= 0) {
                JOptionPane.showMessageDialog(this,
                        "원금과 기간은 0보다 커야 합니다.",
                        "입력 오류",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            BokRiGaeSan calc = new BokRiGaeSan(principal, percent, days);

            String summary = calc.buildSummaryString();
            String daily = calc.buildDailyGrowthString();

            resultArea.setText(summary + "\n\n" + daily);
            resultArea.setCaretPosition(0); // 맨 위로 스크롤

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "입력이 올바른 숫자인지 확인하세요.\n(쉼표 제거, 공백 제거 후 입력)",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "알 수 없는 오류가 발생했습니다:\n" + ex.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // UI 스레드에서 실행
        SwingUtilities.invokeLater(() -> {
            BokRiGuiMain frame = new BokRiGuiMain();
            frame.setVisible(true);
        });
    }
}
