package bokri;

import java.util.Scanner;

public class BoRiShow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== 복리 계산기 ===");

        System.out.print("원금을 입력하세요 (원): ");
        long principal = sc.nextLong();

        System.out.print("하루 수익률을 입력하세요 (%): ");
        double percent = sc.nextDouble();

        System.out.print("기간을 입력하세요 (일): ");
        int days = sc.nextInt();

        BokRiGaeSan calc = new BokRiGaeSan(principal, percent, days);

        System.out.println();
        System.out.println("=== 결과 ===");
        System.out.println(calc);  // toString() 자동 호출

        System.out.println();
        System.out.println("=== 일별 금액 변화 ===");
        calc.printDailyGrowth();

        sc.close();
    }
}
