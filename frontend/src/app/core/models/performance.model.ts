export interface PerformanceStatistics {
    timestamp: string;
    uptime: string;
    database: DatabaseStatistics;
    endpoints: { [key: string]: EndpointStatistics };
    jvm: JvmStatistics;
}

export interface DatabaseStatistics {
    totalRecords: { [key: string]: number };
    queryStats: QueryStatistics;
    connectionPool: ConnectionPoolStatistics;
}

export interface QueryStatistics {
    avgExecutionTime: number;
    slowQueries: number;
    totalQueries: number;
    cacheHitRate: number;
}

export interface ConnectionPoolStatistics {
    active: number;
    idle: number;
    max: number;
    waiting: number;
}

export interface EndpointStatistics {
    avgResponseTime: number;
    p95ResponseTime: number;
    requestCount: number;
    errorRate: number;
}

export interface JvmStatistics {
    memoryUsed: number;
    memoryMax: number;
    gcCount: number;
    threadCount: number;
}
