package bokri;

import java.text.DecimalFormat;

public class BokRiGaeSan {
    private long principal;     // 원금 (KRW)
    private double percent;     // 하루 수익률 (%)
    private int days;           // 기간 (일)

    private static final double EXCHANGE_RATE = 1400.0; // 1 USDT = 1400원 기준

    private DecimalFormat krwDf = new DecimalFormat("#,###");
    private DecimalFormat usdDf = new DecimalFormat("#,###.##");

    public BokRiGaeSan(long principal, double percent, int days) {
        this.principal = principal;
        this.percent = percent;
        this.days = days;
    }

    public long getPrincipal() {
        return principal;
    }

    public void setPrincipal(long principal) {
        this.principal = principal;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
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

    // 복리 총액 계산: 원금 * (1 + percent/100)^days
    public double calculateTotal() {
        return principal * Math.pow(1 + (percent / 100), days);
    }

    // 총액 숫자 KRW
    public String getFormattedTotalNumber() {
        return formatMoneyNumber(calculateTotal());
    }

    // 총액 한글 KRW
    public String getFormattedTotalKorean() {
        long totalRounded = Math.round(calculateTotal());
        return formatMoneyKorean(totalRounded);
    }

    // 총액 USDT
    public String getFormattedTotalUsd() {
        double usd = toUsd(calculateTotal());
        return formatUsd(usd);
    }

    // 하루 단위 금액 변화 출력 (KRW + 한글 + USDT)
    public void printDailyGrowth() {
        double amount = principal;

        for (int i = 1; i <= days; i++) {
            amount *= (1 + percent / 100);
            long rounded = Math.round(amount);
            double usd = toUsd(amount);

            System.out.printf(
                "%d일차 : %s (%s, %s)%n",
                i,
                formatMoneyNumber(amount),       // 123,456원
                formatMoneyKorean(rounded),      // 12만 3천원
                formatUsd(usd)                   // 123.45USDT
            );
        }
    }

    @Override
    public String toString() {
        return String.format(
            "%s으로 %d일 동안 매일 %.3f%%씩 수익이면%n" +
            "→ 총액(숫자 KRW) : %s%n" +
            "→ 총액(한글 KRW) : %s%n" +
            "→ 총액(USDT)     : %s (환율 1USDT = %.0f원 기준)",
            formatMoneyNumber(principal),
            days,
            percent,
            getFormattedTotalNumber(),
            getFormattedTotalKorean(),
            getFormattedTotalUsd(),
            EXCHANGE_RATE
        );
    }
}
