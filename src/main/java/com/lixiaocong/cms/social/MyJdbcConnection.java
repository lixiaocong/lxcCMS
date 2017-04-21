/*
  BSD 3-Clause License

  Copyright (c) 2016, lixiaocong(lxccs@iCloud.com)
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

  * Neither the name of the copyright holder nor the names of its
    contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lixiaocong.cms.social;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public class MyJdbcConnection implements ConnectionRepository {

    private final ServiceProviderConnectionMapper connectionMapper = new ServiceProviderConnectionMapper();
    private String userId;
    private JdbcTemplate jdbcTemplate;
    private ConnectionFactoryLocator connectionFactoryLocator;
    private TextEncryptor textEncryptor;
    private String tablePrefix;

    public MyJdbcConnection(String userId, JdbcTemplate jdbcTemplate, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, String tablePrefix) {
        this.userId = userId;
        this.jdbcTemplate = jdbcTemplate;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.tablePrefix = tablePrefix;
    }

    public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<Connection<?>> resultList = jdbcTemplate.query(selectFromUserConnection() + " where userId = ? order by providerId, rank", connectionMapper, userId);
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    public List<Connection<?>> findConnections(String providerId) {
        return jdbcTemplate.query(selectFromUserConnection() + " where userId = ? and providerId = ? order by rank", connectionMapper, userId, providerId);
    }

    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
        StringBuilder providerUsersCriteriaSql = new StringBuilder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            providerUsersCriteriaSql.append("providerId = :providerId_").append(providerId).append(" and providerUserId in (:providerUserIds_").append(providerId).append(")");
            parameters.addValue("providerId_" + providerId, providerId);
            parameters.addValue("providerUserIds_" + providerId, entry.getValue());
            if (it.hasNext()) {
                providerUsersCriteriaSql.append(" or ");
            }
        }
        List<Connection<?>> resultList = new NamedParameterJdbcTemplate(jdbcTemplate).query(selectFromUserConnection() + " where userId = :userId and " + providerUsersCriteriaSql + " order by providerId, rank", parameters, connectionMapper);
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    public Connection<?> getConnection(ConnectionKey connectionKey) {
        try {
            return jdbcTemplate.queryForObject(selectFromUserConnection() + " where userId = ? and providerId = ? and providerUserId = ?", connectionMapper, userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Transactional
    public void addConnection(Connection<?> connection) {
        try {
            ConnectionData data = connection.createData();
            int rank = jdbcTemplate.queryForObject("select coalesce(max(rank) + 1, 1) as rank from " + tablePrefix + "UserConnection where userId = ? and providerId = ?", new Object[]{userId, data.getProviderId()}, Integer.class);
            jdbcTemplate.update("insert into " + tablePrefix + "UserConnection (userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", userId, data.getProviderId(), data.getProviderUserId(), rank, data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()), encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime());
        } catch (DuplicateKeyException e) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    @Transactional
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        jdbcTemplate.update("update " + tablePrefix + "UserConnection set displayName = ?, profileUrl = ?, imageUrl = ?, accessToken = ?, secret = ?, refreshToken = ?, expireTime = ? where userId = ? and providerId = ? and providerUserId = ?", data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()), encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime(), userId, data.getProviderId(), data.getProviderUserId());
    }

    @Transactional
    public void removeConnections(String providerId) {
        jdbcTemplate.update("delete from " + tablePrefix + "UserConnection where userId = ? and providerId = ?", userId, providerId);
    }

    // internal helpers

    @Transactional
    public void removeConnection(ConnectionKey connectionKey) {
        jdbcTemplate.update("delete from " + tablePrefix + "UserConnection where userId = ? and providerId = ? and providerUserId = ?", userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    private String selectFromUserConnection() {
        return "select userId, providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime from " + tablePrefix + "UserConnection";
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = jdbcTemplate.query(selectFromUserConnection() + " where userId = ? and providerId = ? order by rank", connectionMapper, userId, providerId);
        if (connections.size() > 0) {
            return connections.get(0);
        } else {
            return null;
        }
    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }

    private final class ServiceProviderConnectionMapper implements RowMapper<Connection<?>> {

        public Connection<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConnectionData connectionData = mapConnectionData(rs);
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
            return connectionFactory.createConnection(connectionData);
        }

        private ConnectionData mapConnectionData(ResultSet rs) throws SQLException {
            return new ConnectionData(rs.getString("providerId"), rs.getString("providerUserId"), rs.getString("displayName"), rs.getString("profileUrl"), rs.getString("imageUrl"), decrypt(rs.getString("accessToken")), decrypt(rs.getString("secret")), decrypt(rs.getString("refreshToken")), expireTime(rs.getLong("expireTime")));
        }

        private String decrypt(String encryptedText) {
            return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
        }

        private Long expireTime(long expireTime) {
            return expireTime == 0 ? null : expireTime;
        }

    }

}
