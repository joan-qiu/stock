package com.qqy.stockWealth.dal;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * c3p0数据库连接池配置 Created by jianfei.chen on 2016/12/13.
 * <p>
 * Title: DatasourceConfig
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author jianfei.chen
 * @date 2019年7月22日
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.qqy.stockWealth", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DatasourceConfig {

	@Value("${druid.driverClass:com.mysql.jdbc.Driver}")
	private String driverClass;

	@Value("${druid.jdbcUrl}")
	private String jdbcUrl;

	@Value("${druid.user}")
	private String user;

	@Value("${druid.password}")
	private String password;

	// 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
	@Value("${druid.initialSize:3}")
	private int initialSize;

	// 最小连接池数量
	@Value("${druid.minIdle:3}")
	private int minIdle;

	// 最大连接池数量
	@Value("${druid.maxActive:10}")
	private int maxActive;

	// 获取连接时最大等待时间，单位毫秒
	@Value("${druid.maxWait:60000}")
	private long maxWait;

	// 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，
	// testOnBorrow、testOnReturn、testWhileIdle都不会其作用
	@Value("${druid.validationQuery:select 1}")
	private String validationQuery;

	// 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于
	// timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
	@Value("${druid.testWhileIdle:true}")
	private boolean testWhileIdle;

	// 配置间隔多久才进行一次检测,检测需要关闭的空闲连接,单位是毫秒。有两个含义：
	// 1) Destroy线程会检测连接的间隔时间,;
	// 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
	@Value("${druid.timeBetweenEvictionRunsMillis:60000}")
	private long timeBetweenEvictionRunsMillis;

	// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
	@Value("${druid.testOnBorrow:false}")
	private boolean testOnBorrow;

	// 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
	@Value("${druid.testOnReturn:false}")
	private boolean testOnReturn;

	// 配置一个连接在池中最小生存的时间，单位是毫秒
	@Value("${druid.minEvictableIdleTimeMillis:300000}")
	private long minEvictableIdleTimeMillis;

	@Value("${druid.poolPreparedStatements:false}")
	// 打开PSCache，并且指定每个连接上PSCache的大小
	private boolean poolPreparedStatements;

	@Value("${druid.maxPoolPreparedStatementPerConnectionSize:20}")
	private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${druid.filters:stat,wall,slf4j}")

	// 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
	private String filters;
	@Value("${druid.connectionProperties:oracle.net.CONNECT_TIMEOUT=60000;oracle.jdbc.ReadTimeout=300000;druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000}")

	// 通过connectProperties属性来打开mergeSql功能；慢SQL记录
	private String connectionProperties;
	@Value("${druid.useGlobalDataSourceStat:true}")

	// 合并多个DruidDataSource的监控数据
	private boolean useGlobalDataSourceStat;
	@Value("${druid.keepAlive:true}")
	private boolean keepAlive;

	@Bean(initMethod = "init", destroyMethod = "close")
	public DataSource dataSource() {

		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(jdbcUrl);
		datasource.setUsername(user);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClass);
		// configuration
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setKeepAlive(keepAlive);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);

		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			System.err.println("druid configuration initialization filter: " + e);
		}
		datasource.setConnectionProperties(connectionProperties);
		return datasource;
	}

	@Bean(value = "sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}

	@Autowired
	@Qualifier("masterPageInterceptor")
	PageInterceptor pageInterceptor;

	@Bean(value = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.qqy.stockWealth.dal.mapper,com.qqy.stockWealth.dal.repository");
		String[] mapperLocations = new String[2];
		mapperLocations[0] = "classpath*:com/qqy/stockWealth/dal/mapper/**/*Mapper.xml";
		mapperLocations[1] = "classpath*:com/qqy/stockWealth/dal/repository/**/*.xml";
		sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(mapperLocations));
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageInterceptor });
		return sqlSessionFactoryBean.getObject();

	}

	public Resource[] resolveMapperLocations(String[] mapperLocations) {
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		List<Resource> resources = new ArrayList<Resource>();
		if (mapperLocations != null) {
			for (String mapperLocation : mapperLocations) {
				try {
					Resource[] mappers = resourceResolver.getResources(mapperLocation);
					resources.addAll(Arrays.asList(mappers));
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return resources.toArray(new Resource[resources.size()]);
	}

	@Bean(value = "transactionTemplate")
	public TransactionTemplate transactionTemplate(
			@Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate();
		transactionTemplate.setTransactionManager(transactionManager);
		return transactionTemplate;
	}

	@Bean(value = "transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}

}
