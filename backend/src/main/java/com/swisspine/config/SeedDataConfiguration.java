package com.swisspine.config;

import com.swisspine.entity.*;
import com.swisspine.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Seed data generator for performance testing.
 * Generates thousands of realistic records across all entities.
 * 
 * Run with: --spring.profiles.active=seed-data
 * 
 * @author SwissPine Engineering Team
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfiguration {

    private final ExternalConnectionRepository connectionRepository;
    private final FundRepository fundRepository;
    private final FundAliasRepository fundAliasRepository;
    private final SourceNameRepository sourceNameRepository;
    private final RunNameRepository runNameRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportNameRepository reportNameRepository;
    private final PlannerRepository plannerRepository;

    private static final Random RANDOM = new Random();

    // Realistic data arrays
    private static final String[] ADJECTIVES = {
            "Strategic", "Global", "Dynamic", "Innovative", "Enhanced", "Advanced", "Optimized",
            "Premium", "Elite", "Professional", "Corporate", "International", "Regional", "Hybrid",
            "Diversified", "Balanced", "Growth", "Conservative", "Aggressive", "Moderate"
    };

    private static final String[] FUND_TYPES = {
            "Equity", "Bond", "Balanced", "Index", "Sector", "Growth", "Value", "Income",
            "International", "Emerging Markets", "Real Estate", "Commodity", "Alternative"
    };

    private static final String[] REGIONS = {
            "North America", "Europe", "Asia Pacific", "Latin America", "Middle East",
            "Global", "USA", "UK", "Japan", "China", "India", "Brazil", "Switzerland"
    };

    private static final String[] PLANNER_TYPES = {
            "Standard", "Custom", "Template", "Automated", "Manual", "Scheduled"
    };

    private static final String[] STATUSES = {
            "Draft", "In Progress", "Finished", "Failed"
    };

    private static final String[] SOURCE_TYPES = {
            "Bloomberg Terminal", "Reuters Eikon", "FactSet", "Morningstar Direct",
            "S&P Capital IQ", "Thomson ONE", "PortfolioMetrix", "Advent Geneva",
            "Charles River IMS", "SimCorp Dimension", "BlackRock Aladdin", "State Street"
    };

    private static final String[] RUN_TYPES = {
            "Daily EOD", "Weekly Report", "Monthly Reconciliation", "Quarterly Review",
            "Annual Audit", "Intraday Update", "Real-time Feed", "Batch Process",
            "Ad-hoc Analysis", "Compliance Check", "Risk Assessment", "Performance Attribution"
    };

    private static final String[] REPORT_CATEGORIES = {
            "Performance", "Risk", "Compliance", "Attribution", "Holdings", "Transactions",
            "Reconciliation", "Valuation", "Cash Flow", "Position", "P&L", "Analytics"
    };

    @Bean
    @Profile("seed-data")
    public CommandLineRunner seedDatabase() {
        return args -> {
            log.info("=== STARTING SEED DATA GENERATION ===");
            long startTime = System.currentTimeMillis();

            generateMasterData();
            generateExternalConnections();
            generatePlanners();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("=== SEED DATA GENERATION COMPLETE ===");
            log.info("Total time: {} seconds", duration / 1000.0);
            logStatistics();
        };
    }

    @Transactional
    protected void generateMasterData() {
        log.info("Generating master data...");

        // Generate Funds (2000+)
        if (fundRepository.count() < 100) {
            log.info("Generating 2000 funds...");
            List<Fund> funds = new ArrayList<>();
            for (int i = 1; i <= 2000; i++) {
                Fund fund = Fund.builder()
                        .name(generateFundName(i))
                        .build();
                funds.add(fund);

                if (i % 500 == 0) {
                    fundRepository.saveAll(funds);
                    funds.clear();
                    log.info("  Saved {} funds", i);
                }
            }
            if (!funds.isEmpty()) {
                fundRepository.saveAll(funds);
            }
            log.info("✓ Generated 2000 funds");
        }

        // Generate Fund Aliases (3000+)
        List<Fund> allFunds = fundRepository.findAll();
        if (fundAliasRepository.count() < 100 && !allFunds.isEmpty()) {
            log.info("Generating 3000 fund aliases...");
            List<FundAlias> aliases = new ArrayList<>();
            for (int i = 1; i <= 3000; i++) {
                Fund randomFund = allFunds.get(RANDOM.nextInt(allFunds.size()));
                FundAlias alias = FundAlias.builder()
                        .fund(randomFund)
                        .aliasName(generateFundAliasName(i, randomFund.getName()))
                        .build();
                aliases.add(alias);

                if (i % 500 == 0) {
                    try {
                        fundAliasRepository.saveAll(aliases);
                        aliases.clear();
                        log.info("  Saved {} aliases", i);
                    } catch (Exception e) {
                        log.warn("Error saving aliases, skipping batch: {}", e.getMessage());
                        aliases.clear();
                    }
                }
            }
            if (!aliases.isEmpty()) {
                try {
                    fundAliasRepository.saveAll(aliases);
                } catch (Exception e) {
                    log.warn("Error saving final alias batch");
                }
            }
            log.info("✓ Generated fund aliases");
        }

        // Generate Source Names (200)
        if (sourceNameRepository.count() < 10) {
            log.info("Generating 200 source names...");
            List<SourceName> sources = new ArrayList<>();
            for (int i = 1; i <= 200; i++) {
                sources.add(SourceName.builder()
                        .name(generateSourceName(i))
                        .build());
            }
            sourceNameRepository.saveAll(sources);
            log.info("✓ Generated 200 source names");
        }

        // Generate Run Names (300)
        if (runNameRepository.count() < 10) {
            log.info("Generating 300 run names...");
            List<RunName> runs = new ArrayList<>();
            for (int i = 1; i <= 300; i++) {
                runs.add(RunName.builder()
                        .name(generateRunName(i))
                        .build());
            }
            runNameRepository.saveAll(runs);
            log.info("✓ Generated 300 run names");
        }

        // Generate Report Types (50)
        if (reportTypeRepository.count() < 5) {
            log.info("Generating 50 report types...");
            List<ReportType> types = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                types.add(ReportType.builder()
                        .name(generateReportType(i))
                        .build());
            }
            reportTypeRepository.saveAll(types);
            log.info("✓ Generated 50 report types");
        }

        // Generate Report Names (500)
        List<ReportType> allTypes = reportTypeRepository.findAll();
        if (reportNameRepository.count() < 10 && !allTypes.isEmpty()) {
            log.info("Generating 500 report names...");
            List<ReportName> names = new ArrayList<>();
            for (int i = 1; i <= 500; i++) {
                ReportType randomType = allTypes.get(RANDOM.nextInt(allTypes.size()));
                names.add(ReportName.builder()
                        .name(generateReportName(i, randomType.getName()))
                        .reportType(randomType)
                        .build());
            }
            reportNameRepository.saveAll(names);
            log.info("✓ Generated 500 report names");
        }
    }

    @Transactional
    protected void generateExternalConnections() {
        if (connectionRepository.count() < 10) {
            log.info("Generating 100 external connections...");
            List<ExternalConnection> connections = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                ExternalConnection conn = ExternalConnection.builder()
                        .name(generateConnectionName(i))
                        .baseUrl(generateBaseUrl(i))
                        .authenticationMethod(
                                randomChoice(new String[] { "API Key", "Bearer Token", "OAuth", "Basic Auth" }))
                        .keyField(
                                randomChoice(new String[] { "X-API-Key", "Authorization", "api-key", "access_token" }))
                        .valueField(UUID.randomUUID().toString())
                        .valueFieldSet(true)
                        .authenticationPlace(randomChoice(new String[] { "Header", "QueryParameters" }))
                        .isDefault(i == 1)
                        .build();
                connections.add(conn);
            }

            connectionRepository.saveAll(connections);
            log.info("✓ Generated 100 external connections");
        }
    }

    @Transactional
    protected void generatePlanners() {
        if (plannerRepository.count() < 100) {
            log.info("Generating 5000 planners with associated data...");

            List<ExternalConnection> connections = connectionRepository.findAll();
            List<Fund> funds = fundRepository.findAll();
            List<SourceName> sources = sourceNameRepository.findAll();
            List<RunName> runs = runNameRepository.findAll();
            List<ReportType> reportTypes = reportTypeRepository.findAll();
            List<ReportName> reportNames = reportNameRepository.findAll();

            if (connections.isEmpty() || funds.isEmpty()) {
                log.warn("Cannot generate planners - missing dependencies");
                return;
            }

            int batchSize = 100;
            for (int batch = 0; batch < 50; batch++) {
                List<Planner> planners = new ArrayList<>();

                for (int i = 1; i <= batchSize; i++) {
                    int plannerId = batch * batchSize + i;

                    Planner planner = Planner.builder()
                            .name(generatePlannerName(plannerId))
                            .description(generatePlannerDescription(plannerId))
                            .plannerType(randomChoice(PLANNER_TYPES))
                            .status(randomWeightedStatus())
                            .externalSystemConfig(randomChoice(connections))
                            .build();

                    // Add 1-5 unique funds per planner
                    int numFunds = RANDOM.nextInt(5) + 1;
                    Set<Long> addedFundIds = new HashSet<>();
                    int attempts = 0;
                    while (addedFundIds.size() < numFunds && attempts < numFunds * 3) {
                        Fund randomFund = randomChoice(funds);
                        if (!addedFundIds.contains(randomFund.getId())) {
                            PlannerFund pf = PlannerFund.builder()
                                    .planner(planner)
                                    .fund(randomFund)
                                    .build();
                            planner.addFund(pf);
                            addedFundIds.add(randomFund.getId());
                        }
                        attempts++;
                    }

                    // Add 1-3 sources per planner
                    if (!sources.isEmpty()) {
                        int numSources = RANDOM.nextInt(3) + 1;
                        for (int s = 0; s < numSources; s++) {
                            PlannerSource ps = PlannerSource.builder()
                                    .planner(planner)
                                    .sourceName(randomChoice(sources))
                                    .displayOrder(s + 1)
                                    .build();

                            // Add 1-2 runs per source
                            if (!runs.isEmpty()) {
                                int numRuns = RANDOM.nextInt(2) + 1;
                                for (int r = 0; r < numRuns; r++) {
                                    PlannerRun pr = PlannerRun.builder()
                                            .plannerSource(ps)
                                            .runName(randomChoice(runs))
                                            .displayOrder(r + 1)
                                            .build();
                                    ps.addRun(pr);
                                }
                            }

                            // Add 1-2 reports per source
                            if (!reportNames.isEmpty()) {
                                int numReports = RANDOM.nextInt(2) + 1;
                                for (int rp = 0; rp < numReports; rp++) {
                                    ReportName reportName = randomChoice(reportNames);
                                    PlannerReport pr = PlannerReport.builder()
                                            .plannerSource(ps)
                                            .reportType(reportName.getReportType())
                                            .reportName(reportName)
                                            .displayOrder(rp + 1)
                                            .build();
                                    ps.addReport(pr);
                                }
                            }

                            planner.addSource(ps);
                        }
                    }

                    // Set finishedAt for finished planners
                    if ("Finished".equals(planner.getStatus())) {
                        planner.setFinishedAt(randomPastInstant());
                    }

                    planners.add(planner);
                }

                plannerRepository.saveAll(planners);
                log.info("  Saved batch {}/50 ({} planners total)", batch + 1, (batch + 1) * batchSize);
            }

            log.info("✓ Generated 5000 planners with funds, sources, runs, and reports");
        }
    }

    private void logStatistics() {
        log.info("\n=== DATABASE STATISTICS ===");
        log.info("Funds: {}", fundRepository.count());
        log.info("Fund Aliases: {}", fundAliasRepository.count());
        log.info("Source Names: {}", sourceNameRepository.count());
        log.info("Run Names: {}", runNameRepository.count());
        log.info("Report Types: {}", reportTypeRepository.count());
        log.info("Report Names: {}", reportNameRepository.count());
        log.info("External Connections: {}", connectionRepository.count());
        log.info("Planners: {}", plannerRepository.count());
        log.info("===========================\n");
    }

    // Helper methods for generating realistic data

    private String generateFundName(int id) {
        String adj = randomChoice(ADJECTIVES);
        String type = randomChoice(FUND_TYPES);
        String region = randomChoice(REGIONS);
        return String.format("%s %s %s Fund %d", adj, region, type, id);
    }

    private String generateFundAliasName(int id, String fundName) {
        String[] prefixes = { "Alt-", "Alias-", "Code-", "Ticker-", "ISIN-" };
        return randomChoice(prefixes) + fundName.substring(0, Math.min(20, fundName.length())) + "-" + id;
    }

    private String generateSourceName(int id) {
        return randomChoice(SOURCE_TYPES) + " " + id;
    }

    private String generateRunName(int id) {
        return randomChoice(RUN_TYPES) + " Run " + id;
    }

    private String generateReportType(int id) {
        if (id <= REPORT_CATEGORIES.length) {
            return REPORT_CATEGORIES[id - 1];
        }
        return randomChoice(REPORT_CATEGORIES) + " Type " + id;
    }

    private String generateReportName(int id, String typeName) {
        String[] formats = { "Summary", "Detail", "Consolidated", "Breakdown", "Analysis" };
        return typeName + " " + randomChoice(formats) + " Report " + id;
    }

    private String generateConnectionName(int id) {
        String[] providers = { "Bloomberg", "Reuters", "FactSet", "Morningstar", "S&P", "Refinitiv" };
        String[] environments = { "Production", "UAT", "Staging", "Development" };
        return randomChoice(providers) + " " + randomChoice(environments) + " API " + id;
    }

    private String generateBaseUrl(int id) {
        String[] domains = { "api.bloomberg.com", "api.reuters.com", "api.factset.com",
                "api.morningstar.com", "api.spglobal.com", "api.refinitiv.com" };
        return "https://" + randomChoice(domains) + "/v" + (RANDOM.nextInt(3) + 1) + "/endpoint-" + id;
    }

    private String generatePlannerName(int id) {
        String adj = randomChoice(ADJECTIVES);
        String region = randomChoice(REGIONS);
        return String.format("%s %s Portfolio Planner #%d", adj, region, id);
    }

    private String generatePlannerDescription(int id) {
        String[] purposes = {
                "Automated portfolio rebalancing and optimization",
                "Daily performance monitoring and attribution",
                "Risk analysis and compliance reporting",
                "Multi-asset class portfolio construction",
                "Benchmark-relative performance tracking"
        };
        return randomChoice(purposes) + " for planner instance " + id;
    }

    private String randomWeightedStatus() {
        int rand = RANDOM.nextInt(100);
        if (rand < 40)
            return "Finished"; // 40%
        if (rand < 70)
            return "In Progress"; // 30%
        if (rand < 90)
            return "Draft"; // 20%
        return "Failed"; // 10%
    }

    private Instant randomPastInstant() {
        long daysAgo = RANDOM.nextInt(365);
        return Instant.now().minus(daysAgo, ChronoUnit.DAYS);
    }

    private <T> T randomChoice(T[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private <T> T randomChoice(List<T> list) {
        if (list.isEmpty())
            return null;
        return list.get(RANDOM.nextInt(list.size()));
    }
}
