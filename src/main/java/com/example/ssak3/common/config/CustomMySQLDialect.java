package com.example.ssak3.common.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySQLDialect extends MySQLDialect {

//    @Override
//    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
//        super.initializeFunctionRegistry(functionContributions);
//
//        functionContributions.getFunctionRegistry()
//                .registerPattern(
//                        "match_against",
//                        "match(?1) against (?2 in boolean mode)",
//                        functionContributions.getTypeConfiguration()
//                                .getBasicTypeRegistry()
//                                .resolve(StandardBasicTypes.DOUBLE)
//                );
//    }

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);

        functionContributions.getFunctionRegistry()
                .registerPattern(
                        "match_against",
                        "match(?1) against (?2)",
                        functionContributions.getTypeConfiguration()
                                .getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.DOUBLE)
                );
    }
}