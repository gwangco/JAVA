package bokri;

import java.text.DecimalFormat;

public class BokRiGaeSan {
    private long principal;     // 원금 (KRW)
    private double percent;     // 하루 수익률 (%)
    private int days;           // 기간 (일)

    private static final double EXCHANGE_RATE = 1400.0; // 1 USDT = 1400원

    private DecimalFormat krwDf = new DecimalFormat("#,###");
    private DecimalFormat usdDf = new DecimalFormat("#,###.##");

    public BokRiGaeSan(long principal, double percent, int days) {
        this.principal = principal;
        this.percent = percent;
        this.days = days;
    }

    // 숫자형 금액 포맷: 123,456원
    private String formatMoneyNumber(double amount) {
        return krwDf.format(Math.round(amount)) + "원";
    }

    // 한글 금액 포맷: 1억 2345만 6789원
    private String formatMoneyKorean(long amount) {
        if (amount == 0) return "0원";

        StringBuilder sb = new StringBuilder();

        long jo   = amount / 1_0000_0000_0000L;   // 1조
        long rest = amount % 1_0000_0000_0000L;

        long eok  = rest / 100_000_000L;         // 1억
        rest      = rest % 100_000_000L;

        long man  = rest / 10_000L;              // 1만
        long won  = rest % 10_000L;

        boolean hasUnit = false;

        if (jo > 0) {
            sb.append(jo).append("조 ");
            hasUnit = true;
        }
        if (eok > 0) {
            sb.append(eok).append("억 ");
            hasUnit = true;
        }
        if (man > 0) {
            sb.append(man).append("만 ");
            hasUnit = true;
        }

        if (won > 0) {
            sb.append(won).append("원");
        } else {
            if (hasUnit) {
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("원");
            } else {
                sb.append("0원");
            }
        }

        return sb.toString();
    }

    // KRW → USDT 환산
    private double toUsd(double krwAmount) {
        return krwAmount / EXCHANGE_RATE;
    }

    // USDT 포맷: 1,000USDT or 1,234.56USDT
    private String formatUsd(double usdAmount) {
        return usdDf.format(usdAmount) + "USDT";
    }

    // 복리 총액 계산
    public double calculateTotal() {
        return principal * Math.pow(1 + (percent / 100), days);
    }

    // 요약 문자열 (총액)
    public String buildSummaryString() {
        double total = calculateTotal();
        long totalRounded = Math.round(total);
        double totalUsd = toUsd(total);

        StringBuilder sb = new StringBuilder();
        sb.append("원금: ").append(formatMoneyNumber(principal)).append("\n");
        sb.append("기간: ").append(days).append("일\n");
        sb.append("하루 수익률: ").append(String.format("%.3f", percent)).append("%\n\n");

        sb.append("[결과]\n");
        sb.append("총액(숫자 KRW): ").append(formatMoneyNumber(total)).append("\n");
        sb.append("총액(한글 KRW): ").append(formatMoneyKorean(totalRounded)).append("\n");
        sb.append("총액(USDT): ").append(formatUsd(totalUsd))
          .append(" (환율 1USDT = ").append((int)EXCHANGE_RATE).append("원 기준)\n");

        return sb.toString();
    }

    // 일별 변화 문자열
    public String buildDailyGrowthString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[일별 금액 변화]\n");

        double amount = principal;

        for (int i = 1; i <= days; i++) {
            amount *= (1 + percent / 100);
            long rounded = Math.round(amount);
            double usd = toUsd(amount);

            sb.append(i).append("일차 : ")
              .append(formatMoneyNumber(amount))   // 123,456원
              .append(" (")
              .append(formatMoneyKorean(rounded))  // 12만 3천원
              .append(", ")
              .append(formatUsd(usd))              // 123.45USDT
              .append(")\n");
        }

        return sb.toString();
    }
}
