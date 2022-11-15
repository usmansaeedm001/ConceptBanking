package com.mus.composite.composite.customer;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
public class CompositeStructureInput {
	private String name = "Customer";
	private String parent = "";
	private String childList = "Account,Card";
	private String basePackage = "com.mus.composite";
	private String integrationPackage = "com.mus.composite.integration";
	private String apiContextPath = "/api/composite/customer";
	private String mainComponent = "Customer";
}

