package org.masonord.repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

public class ChatRepository {

    private final DataSource dataSource;

    public ChatRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


}
