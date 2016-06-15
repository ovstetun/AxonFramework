package org.axonframework.eventsourcing.eventstore.jdbc;

import org.axonframework.common.jdbc.Oracle11Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Oracle 11 doesn't support the data type BIGINT, so NUMBER(19) is used as a substitute instead. Also Oracle doesn't
 * seem to like colons in create table statements, so those have been removed.
 */
public class Oracle11EventSchemaFactory extends AbstractEventSchemaFactory {

    @Override
    public PreparedStatement createDomainEventTable(Connection connection, EventSchema configuration) throws SQLException {
        super.createDomainEventTable(connection, configuration)
            .execute();

        Oracle11Utils.simulateAutoIncrement(connection, configuration.domainEventTable(), configuration.globalIndexColumn());

        return Oracle11Utils.createNullStatement(connection);
    }

    @Override
    protected String autoIncrement() {
        return "";
    }

    @Override
    protected String numericDataType() {
        return "NUMBER(19)";
    }

    @Override
    protected String payloadType() {
        return "BLOB";
    }
}
