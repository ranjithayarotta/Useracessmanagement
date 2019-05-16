package com.mindlabs.reportgenearator.model;

public class RiskProfiling {

	private String acc_no;
	private String accClass;
	private float tenure;
	private int txnVol;
	private float trunOver;
	private int riskScore;

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getAccClass() {
		return accClass;
	}

	public void setAccClass(String accClass) {
		this.accClass = accClass;
	}

	public float getTenure() {
		return tenure;
	}

	public void setTenure(float tenure) {
		this.tenure = tenure;
	}

	public int getTxnVol() {
		return txnVol;
	}

	public void setTxnVol(int txnVol) {
		this.txnVol = txnVol;
	}

	public float getTrunOver() {
		return trunOver;
	}

	public void setTrunOver(float trunOver) {
		this.trunOver = trunOver;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
	}

}
