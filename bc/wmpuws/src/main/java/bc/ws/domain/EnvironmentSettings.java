package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Environment settings 
 */
@XmlRootElement(name = "environmentSettings")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentSettings extends CommonResult{
	private String terminalId;
	private int userId;
	private String terminalCurrencyCode;
	private boolean calcPointOnTerminal;
	// формат даты
	private String dateFormat;
	// язык интерфейса пользователя
	private String userInterfaceLang;
	// report_format, -- формат вывода отчетов
	private String reportFormat;
	// название схемы БД, в которой хранятся объекты пользователя
	// private String dbSchema;
	// количество строчек на страничку (если требуется постраничный вывод, напр. списка операций)
	// private Integer rowsOnPage;

	public EnvironmentSettings() {
	}

	public EnvironmentSettings(Integer returnCode, String message) {
		super(returnCode, message);
	}

	public EnvironmentSettings(String returnCode, String message) {
		super(returnCode, message);
	}

	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dataFormat) {
		this.dateFormat = dataFormat;
	}
	public String getUserInterfaceLang() {
		return userInterfaceLang;
	}
	public void setUserInterfaceLang(String userInterfaceLang) {
		this.userInterfaceLang = userInterfaceLang;
	}
	public String getReportFormat() {
		return reportFormat;
	}
	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTerminalCurrencyCode() {
		return terminalCurrencyCode;
	}

	public void setTerminalCurrencyCode(String terminalCurrencyCode) {
		this.terminalCurrencyCode = terminalCurrencyCode;
	}

	public boolean isCalcPointOnTerminal() {
		return calcPointOnTerminal;
	}

	public void setCalcPointOnTerminal(boolean calcPointOnTerminal) {
		this.calcPointOnTerminal = calcPointOnTerminal;
	}


    @Override
    public String toString() {
        return "EnvironmentSettings{" +
                "terminalId='" + terminalId + '\'' +
                ", userId=" + userId +
                ", terminalCurrencyCode='" + terminalCurrencyCode + '\'' +
                ", calcPointOnTerminal=" + calcPointOnTerminal +
                ", dateFormat='" + dateFormat + '\'' +
                ", userInterfaceLang='" + userInterfaceLang + '\'' +
                ", reportFormat='" + reportFormat + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnvironmentSettings that = (EnvironmentSettings) o;

        if (userId != that.userId) return false;
        if (calcPointOnTerminal != that.calcPointOnTerminal) return false;
        if (terminalId != null ? !terminalId.equals(that.terminalId) : that.terminalId != null) return false;
        if (terminalCurrencyCode != null ? !terminalCurrencyCode.equals(that.terminalCurrencyCode) : that.terminalCurrencyCode != null)
            return false;
        if (dateFormat != null ? !dateFormat.equals(that.dateFormat) : that.dateFormat != null) return false;
        if (userInterfaceLang != null ? !userInterfaceLang.equals(that.userInterfaceLang) : that.userInterfaceLang != null)
            return false;
        return reportFormat != null ? reportFormat.equals(that.reportFormat) : that.reportFormat == null;

    }

    @Override
    public int hashCode() {
        int result = terminalId != null ? terminalId.hashCode() : 0;
        result = 31 * result + userId;
        result = 31 * result + (terminalCurrencyCode != null ? terminalCurrencyCode.hashCode() : 0);
        result = 31 * result + (calcPointOnTerminal ? 1 : 0);
        result = 31 * result + (dateFormat != null ? dateFormat.hashCode() : 0);
        result = 31 * result + (userInterfaceLang != null ? userInterfaceLang.hashCode() : 0);
        result = 31 * result + (reportFormat != null ? reportFormat.hashCode() : 0);
        return result;
    }
}
