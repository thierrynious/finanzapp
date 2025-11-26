package com.finanzmanager.finanzapp.config;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V3__Add_Tax_Column extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        context.getConnection()
                .createStatement()
                .execute("ALTER TABLE transactions ADD COLUMN tax DECIMAL(10,2)");
    }
}


