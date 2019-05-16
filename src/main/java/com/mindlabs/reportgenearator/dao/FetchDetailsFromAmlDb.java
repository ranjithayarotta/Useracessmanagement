package com.mindlabs.reportgenearator.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mindlabs.reportgenearator.model.LargeCashTransactions;
import com.mindlabs.reportgenearator.model.RiskProfiling;
import com.mindlabs.reportgenearator.model.SuspeciousTransaction;

public class FetchDetailsFromAmlDb {

	public List<RiskProfiling> getRiskMatchWithPriorityValueInfo(Connection con, String query) {
		List<RiskProfiling> riskProfiling = new ArrayList<>();

		try (Statement ps = con.createStatement(); ResultSet rs = ps.executeQuery(query)) {
			while (rs.next()) {
				RiskProfiling risk = new RiskProfiling();
				risk.setAcc_no(rs.getString(1));
				risk.setAccClass(rs.getString(2));
				risk.setRiskScore(rs.getInt(3));
				risk.setTenure(rs.getFloat(4));
				risk.setTrunOver(rs.getFloat(5));
				risk.setTxnVol(rs.getInt(6));
				riskProfiling.add(risk);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return riskProfiling;
	}

	public List<LargeCashTransactions> getLargeCashTransactionInfo(Connection con, String query) {
		List<LargeCashTransactions> largeCashTransactionList = new ArrayList<>();
		try (Statement ps = con.createStatement(); ResultSet rs = ps.executeQuery(query)) {
			while (rs.next()) {

				LargeCashTransactions largCashTransactions = new LargeCashTransactions();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date ds = null;
				Timestamp timestamp = rs.getTimestamp(3);
				if (timestamp != null)
					ds = new java.util.Date(timestamp.getTime());
				String currentTime = sdf.format(ds);
				largCashTransactions.setState(rs.getString(1));
				largCashTransactions.setAccountnumber(rs.getString(2));
				largCashTransactions.setAlertDate(currentTime);
				largCashTransactions.setEntrySrNumber(rs.getInt(4));
				largCashTransactions.setStrCode(rs.getString(5));
				largCashTransactions.setBrachname(rs.getString(6));
				largeCashTransactionList.add(largCashTransactions);

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return largeCashTransactionList;
	}

	public List<SuspeciousTransaction> getSuspeciousCashTransactionInfo(Connection con, String query) {
		List<SuspeciousTransaction> sucpeciousList = new ArrayList<>();
		try (Statement ps = con.createStatement(); ResultSet rs = ps.executeQuery(query)) {
			while (rs.next()) {
				SuspeciousTransaction suspeciousCashTransactions = new SuspeciousTransaction();
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date ds = null;
				Timestamp timestamp = rs.getTimestamp(3);
				if (timestamp != null)
					ds = new java.util.Date(timestamp.getTime());
				String currentTime = sdf.format(ds);
				suspeciousCashTransactions.setState(rs.getString(1));
				suspeciousCashTransactions.setAccountnumber(rs.getString(2));
				suspeciousCashTransactions.setAlertDate(currentTime);
				suspeciousCashTransactions.setEntrySrNumber(rs.getInt(4));
				suspeciousCashTransactions.setStrCode(rs.getString(5));
				suspeciousCashTransactions.setBrachname(rs.getString(6));
				sucpeciousList.add(suspeciousCashTransactions);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return sucpeciousList;
	}

	public int getTotalCountOfRisk(Connection con, String query) {
		int totalCount = 0;
		try (Statement ps = con.createStatement(); ResultSet rs = ps.executeQuery(query)) {
			while (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return totalCount;
	}

}
