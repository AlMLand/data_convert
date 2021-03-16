package com.m_landalex.dataconvert.jmx;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomStatistics {

	@Autowired
	private SessionFactory sessionFactory;
	private Statistics statistics;
	
	@PostConstruct
	private void init() {
		statistics = sessionFactory.getStatistics();
	}
	
	public EntityStatistics getEntityStatistics(String entityName) {
		return statistics.getEntityStatistics(entityName);
	}

	public CollectionStatistics getCollectionStatistics(String role) {
		return statistics.getCollectionStatistics(role);
	}

	public CacheRegionStatistics getSecondLevelCacheStatistics(String regionName) {
		return statistics.getCacheRegionStatistics(regionName);
	}

	public QueryStatistics getQueryStatistics(String hql) {
		return statistics.getQueryStatistics(hql);
	}

	public long getEntityDeleteCount() {
		return statistics.getEntityDeleteCount();
	}

	public long getEntityInsertCount() {
		return statistics.getEntityInsertCount();
	}

	public long getEntityLoadCount() {
		return statistics.getEntityLoadCount();
	}

	public long getEntityFetchCount() {
		return statistics.getEntityFetchCount();
	}

	public long getEntityUpdateCount() {
		return statistics.getEntityUpdateCount();
	}

	public long getQueryExecutionCount() {
		return statistics.getQueryExecutionCount();
	}

	public long getQueryCacheHitCount() {
		return statistics.getQueryCacheHitCount();
	}

	public long getQueryExecutionMaxTime() {
		return statistics.getQueryExecutionMaxTime();
	}

	public long getQueryCacheMissCount() {
		return statistics.getQueryCacheMissCount();
	}

	public long getQueryCachePutCount() {
		return statistics.getQueryCachePutCount();
	}

	public long getFlushCount() {
		return statistics.getFlushCount();
	}

	public long getConnectCount() {
		return statistics.getConnectCount();
	}

	public long getSecondLevelCacheHitCount() {
		return statistics.getSecondLevelCacheHitCount();
	}

	public long getSecondLevelCacheMissCount() {
		return statistics.getSecondLevelCacheMissCount();
	}

	public long getSecondLevelCachePutCount() {
		return statistics.getSecondLevelCachePutCount();
	}

	public long getSessionCloseCount() {
		return statistics.getSessionCloseCount();
	}

	public long getSessionOpenCount() {
		return statistics.getSessionOpenCount();
	}

	public long getCollectionLoadCount() {
		return statistics.getCollectionLoadCount();
	}

	public long getCollectionFetchCount() {
		return statistics.getCollectionFetchCount();
	}

	public long getCollectionUpdateCount() {
		return statistics.getCollectionUpdateCount();
	}

	public long getCollectionRemoveCount() {
		return statistics.getCollectionRemoveCount();
	}

	public long getCollectionRecreateCount() {
		return statistics.getCollectionRecreateCount();
	}

	public long getStartTime() {
		return statistics.getStartTime();
	}

	public boolean isStatisticsEnabled() {
		return statistics.isStatisticsEnabled();
	}

	public String[] getEntityNames() {
		return statistics.getEntityNames();
	}

	public String[] getQueries() {
		return statistics.getQueries();
	}

	public long getSuccessfulTransactionCount() {
		return statistics.getSuccessfulTransactionCount();
	}
	public long getTransactionCount() {
		return statistics.getTransactionCount();
	}

	public String getQueryExecutionMaxTimeQueryString() {
		return statistics.getQueryExecutionMaxTimeQueryString();
	}
	
}
