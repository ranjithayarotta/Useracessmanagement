package com.mindlabs.reportgenearator.service;

import org.springframework.stereotype.Service;

@Service
public class QueryBuilder {

	public String getQueryForLowPriority(int offsetValue) {
		return "SELECT\n" + "     risk_profiling_table.`CUST_AC_NO` AS ACC_NO,\n"
				+ "     risk_profiling_table.`ACCOUNT_CLASS` AS ACC_CLASS,\n"
				+ "     risk_profiling_table.`TENURE` AS TENURE,\n"
				+ "     risk_profiling_table.`TXN_VOL` AS TXN_VOL,\n"
				+ "     risk_profiling_table.`TURNOVER` AS TURNOVER,\n"
				+ "     risk_profiling_table.`TOTAL_RISK` AS RISK_SCORE\n" + "FROM \n"
				+ "  `risk_profiling_table` WHERE risk_profiling_table.`TOTAL_RISK` BETWEEN 0 AND 7 ORDER BY RSK_PROF_ID ASC LIMIT "
				+ offsetValue + ", " + 10000;
	}

	public String getQueryForMediumRiskPriority(int offsetValue) {

		int endCount = offsetValue + 10000;
		return "SELECT\n" + "     risk_profiling_table.`CUST_AC_NO` AS ACC_NO,\n"
				+ "     risk_profiling_table.`ACCOUNT_CLASS` AS ACC_CLASS,\n"
				+ "     risk_profiling_table.`TENURE` AS TENURE,\n"
				+ "     risk_profiling_table.`TXN_VOL` AS TXN_VOL,\n"
				+ "     risk_profiling_table.`TURNOVER` AS TURNOVER,\n"
				+ "     risk_profiling_table.`TOTAL_RISK` AS RISK_SCORE\n" + "FROM \n"
				+ "  `risk_profiling_table` Where risk_profiling_table.`TOTAL_RISK` BETWEEN 8 AND 15 ORDER BY RSK_PROF_ID ASC LIMIT "
				+ offsetValue + ", " + endCount;
	}

	public String getQueryForHighRiskPriority(int offsetValue) {

		return "SELECT\n" + "     risk_profiling_table.`CUST_AC_NO` AS ACC_NO,\n"
				+ "     risk_profiling_table.`ACCOUNT_CLASS` AS ACC_CLASS,\n"
				+ "     risk_profiling_table.`TENURE` AS TENURE,\n"
				+ "     risk_profiling_table.`TXN_VOL` AS TXN_VOL,\n"
				+ "     risk_profiling_table.`TURNOVER` AS TURNOVER,\n"
				+ "     risk_profiling_table.`TOTAL_RISK` AS RISK_SCORE\n" + "FROM \n"
				+ "  `risk_profiling_table` WHERE  risk_profiling_table.`TOTAL_RISK` > 15 ORDER BY RSK_PROF_ID ASC LIMIT "
				+ offsetValue + ", " + 10000;

	}

	public String getQueryForTotalCountHighRisk() {

		return "SELECT COUNT( * ) \n" + "FROM risk_profiling_table \n"
				+ "Where risk_profiling_table.`TOTAL_RISK` > 15;";

	}

	public String getQueryForTotalCountMedRisk() {

		return "SELECT COUNT( * ) \n" + "FROM risk_profiling_table \n"
				+ "Where risk_profiling_table.`TOTAL_RISK` BETWEEN 8 AND 15;";

	}

	public String getQueryForTotalCountLowRisk() {

		return "SELECT COUNT( * ) \n" + "FROM risk_profiling_table \n"
				+ "Where risk_profiling_table.`TOTAL_RISK` BETWEEN 0 AND 7;";

	}

	public String getQueryForSelectSuspeciousTransaction() {
		return "SELECT\n" + "     alert_table.`state` AS State,\n" + "     alert_table.`accountNumber` AS AccNo,\n"
				+ "     alert_table.`alertDate` AS Date,\n" + "     alert_table.`acEntrySrNo` AS EntrySrNo,\n"
				+ "     alert_table.`strCode` AS STRCode,\n" + "     alert_table.`branch` AS Branch\n" + "FROM\n"
				+ "     `alert_table` alert_table";
	}

	public String getQueryForSelectLargeCashTransations() {
		return "     SELECT\n" + "     alert_table.`state` AS State,\n" + "     alert_table.`accountNumber` AS AccNo,\n"
				+ "     alert_table.`alertDate` AS Date,\n" + "     alert_table.`acEntrySrNo` AS EntrySrNo,\n"
				+ "     alert_table.`strCode` AS STRCode,\n" + "     alert_table.`branch` AS Branch\n" + "FROM\n"
				+ "     `alert_table` alert_table\n" + "WHERE\n" + "     strCode = 'STR05'";
	}

	public String getQueryForTotalCountOfSuspeciousTransaction() {
		return "SELECT COUNT( * ) FROM alert_table alert_table";
	}

	public String getQueryForTotalCountOfLargeCashTransations() {
		return "SELECT COUNT( * ) FROM alert_table alert_table Where strCode = 'STR05';";
	}
}
