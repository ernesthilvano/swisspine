-- Flyway Migration: Seed Data
-- Version: V2
-- Description: Populate database with realistic test data

-- ============================================
-- Master Data: Source Names
-- ============================================

INSERT INTO source_names (name) VALUES
    ('Bloomberg'),
    ('Reuters'),
    ('FactSet'),
    ('Refinitiv'),
    ('S&P Capital IQ'),
    ('Morningstar Direct'),
    ('MSCI'),
    ('FTSE Russell'),
    ('ICE Data Services'),
    ('Thomson Reuters Eikon'),
    ('Interactive Data'),
    ('Markit'),
    ('NASDAQ Data Link'),
    ('CME Group'),
    ('Intercontinental Exchange'),
    ('London Stock Exchange'),
    ('Deutsche Börse'),
    ('Euronext'),
    ('SIX Swiss Exchange'),
    ('Hong Kong Exchanges');

-- ============================================
-- Master Data: Run Names
-- ============================================

INSERT INTO run_names (name) VALUES
    ('Daily EOD'),
    ('Weekly Summary'),
    ('Monthly Close'),
    ('Quarterly Review'),
    ('Annual Report'),
    ('Intraday Real-time'),
    ('Ad-hoc Request'),
    ('Pre-market'),
    ('Post-market'),
    ('Mid-day Snapshot'),
    ('T+1 Settlement'),
    ('T+2 Settlement'),
    ('Month-end Close'),
    ('Quarter-end Close'),
    ('Year-end Close');

-- ============================================
-- Master Data: Report Types
-- ============================================

INSERT INTO report_types (name) VALUES
    ('Performance'),
    ('Risk Analysis'),
    ('Attribution'),
    ('Holdings'),
    ('Compliance'),
    ('Trading Activity'),
    ('Cash Flow'),
    ('Reconciliation'),
    ('Exposure'),
    ('Benchmark Comparison'),
    ('Stress Testing'),
    ('VaR Analysis'),
    ('Portfolio Summary'),
    ('Transaction History'),
    ('Fee Analysis');

-- ============================================
-- Master Data: Report Names
-- ============================================

INSERT INTO report_names (name, report_type_id) VALUES
    -- Performance Reports
    ('Total Return Analysis', (SELECT id FROM report_types WHERE name = 'Performance')),
    ('Time-Weighted Return', (SELECT id FROM report_types WHERE name = 'Performance')),
    ('Money-Weighted Return', (SELECT id FROM report_types WHERE name = 'Performance')),
    ('Benchmark Relative Performance', (SELECT id FROM report_types WHERE name = 'Performance')),
    ('Rolling Returns', (SELECT id FROM report_types WHERE name = 'Performance')),
    
    -- Risk Reports
    ('Portfolio Risk Metrics', (SELECT id FROM report_types WHERE name = 'Risk Analysis')),
    ('Value at Risk (VaR)', (SELECT id FROM report_types WHERE name = 'Risk Analysis')),
    ('Tracking Error', (SELECT id FROM report_types WHERE name = 'Risk Analysis')),
    ('Beta Analysis', (SELECT id FROM report_types WHERE name = 'Risk Analysis')),
    ('Volatility Report', (SELECT id FROM report_types WHERE name = 'Risk Analysis')),
    
    -- Attribution Reports
    ('Asset Allocation Attribution', (SELECT id FROM report_types WHERE name = 'Attribution')),
    ('Security Selection Attribution', (SELECT id FROM report_types WHERE name = 'Attribution')),
    ('Sector Attribution', (SELECT id FROM report_types WHERE name = 'Attribution')),
    ('Geographic Attribution', (SELECT id FROM report_types WHERE name = 'Attribution')),
    ('Currency Attribution', (SELECT id FROM report_types WHERE name = 'Attribution')),
    
    -- Holdings Reports
    ('Current Holdings Summary', (SELECT id FROM report_types WHERE name = 'Holdings')),
    ('Top 10 Holdings', (SELECT id FROM report_types WHERE name = 'Holdings')),
    ('Asset Allocation Breakdown', (SELECT id FROM report_types WHERE name = 'Holdings')),
    ('Sector Allocation', (SELECT id FROM report_types WHERE name = 'Holdings')),
    ('Geographic Distribution', (SELECT id FROM report_types WHERE name = 'Holdings')),
    
    -- Compliance Reports
    ('Regulatory Compliance Check', (SELECT id FROM report_types WHERE name = 'Compliance')),
    ('Investment Guidelines Adherence', (SELECT id FROM report_types WHERE name = 'Compliance')),
    ('Concentration Limits', (SELECT id FROM report_types WHERE name = 'Compliance')),
    ('Prohibited Holdings', (SELECT id FROM report_types WHERE name = 'Compliance')),
    ('ESG Compliance', (SELECT id FROM report_types WHERE name = 'Compliance')),
    
    -- Trading Activity
    ('Daily Trade List', (SELECT id FROM report_types WHERE name = 'Trading Activity')),
    ('Trade Cost Analysis', (SELECT id FROM report_types WHERE name = 'Trading Activity')),
    ('Order Execution Quality', (SELECT id FROM report_types WHERE name = 'Trading Activity')),
    ('Broker Performance', (SELECT id FROM report_types WHERE name = 'Trading Activity')),
    
    -- Cash Flow
    ('Cash Flow Statement', (SELECT id FROM report_types WHERE name = 'Cash Flow')),
    ('Dividend Income', (SELECT id FROM report_types WHERE name = 'Cash Flow')),
    ('Interest Income', (SELECT id FROM report_types WHERE name = 'Cash Flow')),
    ('Capital Calls and Distributions', (SELECT id FROM report_types WHERE name = 'Cash Flow')),
    
    -- Reconciliation
    ('Cash Reconciliation', (SELECT id FROM report_types WHERE name = 'Reconciliation')),
    ('Position Reconciliation', (SELECT id FROM report_types WHERE name = 'Reconciliation')),
    ('NAV Reconciliation', (SELECT id FROM report_types WHERE name = 'Reconciliation')),
    ('Corporate Actions Reconciliation', (SELECT id FROM report_types WHERE name = 'Reconciliation')),
    
    -- Exposure
    ('Gross Exposure', (SELECT id FROM report_types WHERE name = 'Exposure')),
    ('Net Exposure', (SELECT id FROM report_types WHERE name = 'Exposure')),
    ('Currency Exposure', (SELECT id FROM report_types WHERE name = 'Exposure')),
    ('Sector Exposure', (SELECT id FROM report_types WHERE name = 'Exposure')),
    
    -- Stress Testing
    ('Market Stress Scenarios', (SELECT id FROM report_types WHERE name = 'Stress Testing')),
    ('Historical Crisis Simulation', (SELECT id FROM report_types WHERE name = 'Stress Testing')),
    ('Custom Stress Tests', (SELECT id FROM report_types WHERE name = 'Stress Testing'));

-- ============================================
-- Master Data: Funds
-- ============================================

INSERT INTO funds (name) VALUES
    ('Global Equity Fund'),
    ('US Large Cap Growth Fund'),
    ('International Equity Fund'),
    ('Emerging Markets Fund'),
    ('Small Cap Value Fund'),
    ('Investment Grade Bond Fund'),
    ('High Yield Bond Fund'),
    ('Municipal Bond Fund'),
    ('Global Fixed Income Fund'),
    ('Balanced Fund'),
    ('Target Date 2030 Fund'),
    ('Target Date 2040 Fund'),
    ('Target Date 2050 Fund'),
    ('Real Estate Fund'),
    ('Infrastructure Fund'),
    ('Commodities Fund'),
    ('Money Market Fund'),
    ('Short Duration Bond Fund'),
    ('Multi-Asset Income Fund'),
    ('Dividend Growth Fund'),
    ('Technology Sector Fund'),
    ('Healthcare Sector Fund'),
    ('Financial Sector Fund'),
    ('ESG Equity Fund'),
    ('Low Volatility Fund'),
    ('Emerging Markets Bond Fund'),
    ('Convertible Securities Fund'),
    ('Alternative Strategies Fund'),
    ('Private Equity Fund'),
    ('Hedge Fund of Funds');

-- ============================================
-- Master Data: Fund Aliases
-- ============================================

INSERT INTO fund_aliases (fund_id, alias_name) VALUES
    -- Global Equity Fund
    ((SELECT id FROM funds WHERE name = 'Global Equity Fund'), 'GEF Class A'),
    ((SELECT id FROM funds WHERE name = 'Global Equity Fund'), 'GEF Class I'),
    ((SELECT id FROM funds WHERE name = 'Global Equity Fund'), 'GEF Institutional'),
    
    -- US Large Cap Growth Fund
    ((SELECT id FROM funds WHERE name = 'US Large Cap Growth Fund'), 'USLCG Retail'),
    ((SELECT id FROM funds WHERE name = 'US Large Cap Growth Fund'), 'USLCG Institutional'),
    
    -- International Equity Fund
    ((SELECT id FROM funds WHERE name = 'International Equity Fund'), 'INTL EQ Class A'),
    ((SELECT id FROM funds WHERE name = 'International Equity Fund'), 'INTL EQ Class I'),
    ((SELECT id FROM funds WHERE name = 'International Equity Fund'), 'INTL EQ Advisor'),
    
    -- Emerging Markets Fund
    ((SELECT id FROM funds WHERE name = 'Emerging Markets Fund'), 'EM Fund Class A'),
    ((SELECT id FROM funds WHERE name = 'Emerging Markets Fund'), 'EM Fund Institutional'),
    
    -- Investment Grade Bond Fund
    ((SELECT id FROM funds WHERE name = 'Investment Grade Bond Fund'), 'IG Bond Class A'),
    ((SELECT id FROM funds WHERE name = 'Investment Grade Bond Fund'), 'IG Bond Class I'),
    ((SELECT id FROM funds WHERE name = 'Investment Grade Bond Fund'), 'IG Bond Retirement'),
    
    -- Target Date Funds
    ((SELECT id FROM funds WHERE name = 'Target Date 2030 Fund'), 'TDF 2030 Class A'),
    ((SELECT id FROM funds WHERE name = 'Target Date 2030 Fund'), 'TDF 2030 Class I'),
    ((SELECT id FROM funds WHERE name = 'Target Date 2040 Fund'), 'TDF 2040 Class A'),
    ((SELECT id FROM funds WHERE name = 'Target Date 2040 Fund'), 'TDF 2040 Class I'),
    ((SELECT id FROM funds WHERE name = 'Target Date 2050 Fund'), 'TDF 2050 Class A'),
    ((SELECT id FROM funds WHERE name = 'Target Date 2050 Fund'), 'TDF 2050 Class I'),
    
    -- ESG Fund
    ((SELECT id FROM funds WHERE name = 'ESG Equity Fund'), 'ESG Fund Class A'),
    ((SELECT id FROM funds WHERE name = 'ESG Equity Fund'), 'ESG Fund Institutional'),
    ((SELECT id FROM funds WHERE name = 'ESG Equity Fund'), 'ESG Fund Sustainable'),
    
    -- Real Estate Fund
    ((SELECT id FROM funds WHERE name = 'Real Estate Fund'), 'REIT Fund Class A'),
    ((SELECT id FROM funds WHERE name = 'Real Estate Fund'), 'REIT Fund Class I'),
    
    -- Balanced Fund
    ((SELECT id FROM funds WHERE name = 'Balanced Fund'), 'Balanced 60/40 Class A'),
    ((SELECT id FROM funds WHERE name = 'Balanced Fund'), 'Balanced 60/40 Class I');

-- ============================================
-- Sample Data: External Connections
-- ============================================

INSERT INTO external_connections (name, base_url, authentication_method, key_field, value_field, value_field_set, authentication_place, is_default) VALUES
    ('Bloomberg Terminal API', 'https://api.bloomberg.com/v1', 'API Key', 'X-API-Key', 'bbg_api_key_prod_2024', true, 'Header', true),
    ('Reuters DataScope', 'https://selectapi.datascope.reuters.com/RestApi/v1', 'Bearer Token', 'Authorization', 'Bearer eyJhbGci...', true, 'Header', false),
    ('FactSet Analytics API', 'https://api.factset.com/analytics/v3', 'Basic Auth', 'Authorization', 'Basic dXNlcm5hbWU6cGFzc3dvcmQ=', true, 'Header', false),
    ('Morningstar Direct', 'https://direct.morningstar.com/api/v2', 'API Key', 'api_key', 'mstar_key_2024', true, 'QueryParameters', false),
    ('MSCI Factor Data', 'https://api.msci.com/factor/v1', 'Bearer Token', 'Authorization', 'Bearer msci_token_xyz', true, 'Header', false),
    ('ICE Data Services', 'https://api.ice.services.com/v2', 'API Key', 'X-ICE-API-Key', 'ice_api_key_prod', true, 'Header', false),
    ('FTSE Russell Index Data', 'https://api.ftserussell.com/v1', 'OAuth', 'Authorization', 'Bearer ftse_oauth_token', true, 'Header', false),
    ('S&P Global Market Intelligence', 'https://api.spglobal.com/market-intelligence/v1', 'API Key', 'X-SPGMI-Key', 'spgmi_key_2024', true, 'Header', false),
    ('Refinitiv Workspace', 'https://api.refinitiv.com/data/v1', 'Bearer Token', 'Authorization', 'Bearer ref_workspace_token', true, 'Header', false),
    ('Interactive Data Real-Time', 'https://api.interactivedata.com/realtime/v2', 'API Key', 'apiKey', 'idata_realtime_key', true, 'QueryParameters', false);

-- ============================================
-- Sample Data: Planners
-- ============================================

INSERT INTO planners (name, description, planner_type, external_system_config_id, status, owner) VALUES
    ('Daily Performance Reporting', 'End-of-day performance calculation and reporting for all equity funds', 'Performance Analysis', 
     (SELECT id FROM external_connections WHERE name = 'Bloomberg Terminal API'), 'Active', 'John Smith'),
    
    ('Monthly Risk Assessment', 'Comprehensive monthly risk analysis including VaR, tracking error, and stress tests', 'Risk Management',
     (SELECT id FROM external_connections WHERE name = 'MSCI Factor Data'), 'Active', 'Sarah Johnson'),
    
    ('Quarterly Attribution Analysis', 'Detailed performance attribution by asset class, sector, and security selection', 'Attribution',
     (SELECT id FROM external_connections WHERE name = 'FactSet Analytics API'), 'Draft', 'Michael Chen'),
    
    ('Weekly Compliance Check', 'Automated compliance monitoring for investment guidelines and regulatory requirements', 'Compliance',
     (SELECT id FROM external_connections WHERE name = 'Morningstar Direct'), 'Active', 'Emily Davis'),
    
    ('Year-End Holdings Report', 'Comprehensive year-end holdings and allocation reporting for all funds', 'Reporting',
     (SELECT id FROM external_connections WHERE name = 'Bloomberg Terminal API'), 'Completed', 'David Brown'),
    
    ('ESG Score Integration', 'Integration of ESG scores and sustainability metrics for equity portfolios', 'ESG Analysis',
     (SELECT id FROM external_connections WHERE name = 'MSCI Factor Data'), 'Active', 'Lisa Anderson'),
    
    ('Fixed Income Analytics', 'Daily fixed income portfolio analytics including duration, convexity, and spread analysis', 'Analytics',
     (SELECT id FROM external_connections WHERE name = 'ICE Data Services'), 'Active', 'Robert Wilson'),
    
    ('Multi-Asset Allocation', 'Monthly multi-asset class allocation review and rebalancing recommendations', 'Asset Allocation',
     (SELECT id FROM external_connections WHERE name = 'Bloomberg Terminal API'), 'Draft', 'Jennifer Martinez'),
    
    ('Benchmark Comparison Suite', 'Automated benchmark comparison across all fund categories', 'Performance',
     (SELECT id FROM external_connections WHERE name = 'FTSE Russell Index Data'), 'Active', 'Thomas Garcia'),
    
    ('Trade Cost Analysis', 'Daily trade cost analysis and broker performance evaluation', 'Transaction Analysis',
     (SELECT id FROM external_connections WHERE name = 'Refinitiv Workspace'), 'Active', 'Michelle Lee'),
    
    ('Cash Flow Projection', 'Weekly cash flow projection and liquidity management', 'Cash Management',
     (SELECT id FROM external_connections WHERE name = 'Bloomberg Terminal API'), 'Active', 'Christopher Taylor'),
    
    ('Emerging Markets Monitor', 'Real-time monitoring of emerging markets positions and risk exposure', 'Risk Monitoring',
     (SELECT id FROM external_connections WHERE name = 'Reuters DataScope'), 'Draft', 'Amanda White'),
    
    ('Dividend Income Tracker', 'Monthly dividend income tracking and forecasting', 'Income Analysis',
     (SELECT id FROM external_connections WHERE name = 'S&P Global Market Intelligence'), 'Active', 'Daniel Harris'),
    
    ('Currency Exposure Report', 'Daily currency exposure analysis and hedging recommendations', 'FX Analysis',
     (SELECT id FROM external_connections WHERE name = 'Refinitiv Workspace'), 'Active', 'Jessica Clark'),
    
    ('Target Date Fund Rebalancing', 'Quarterly rebalancing analysis for target date fund suite', 'Rebalancing',
     (SELECT id FROM external_connections WHERE name = 'Morningstar Direct'), 'Completed', 'Kevin Rodriguez');

-- ============================================
-- Sample Data: Planner Funds Relationships
-- ============================================

-- Daily Performance Reporting (All Equity Funds)
INSERT INTO planner_funds (planner_id, fund_id) VALUES
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM funds WHERE name = 'Global Equity Fund')),
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM funds WHERE name = 'US Large Cap Growth Fund')),
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM funds WHERE name = 'International Equity Fund')),
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM funds WHERE name = 'Emerging Markets Fund')),
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM funds WHERE name = 'Small Cap Value Fund'));

-- Monthly Risk Assessment
INSERT INTO planner_funds (planner_id, fund_id) VALUES
    ((SELECT id FROM planners WHERE name = 'Monthly Risk Assessment'), (SELECT id FROM funds WHERE name = 'Global Equity Fund')),
    ((SELECT id FROM planners WHERE name = 'Monthly Risk Assessment'), (SELECT id FROM funds WHERE name = 'Balanced Fund')),
    ((SELECT id FROM planners WHERE name = 'Monthly Risk Assessment'), (SELECT id FROM funds WHERE name = 'Alternative Strategies Fund'));

-- ESG Score Integration
INSERT INTO planner_funds (planner_id, fund_id) VALUES
    ((SELECT id FROM planners WHERE name = 'ESG Score Integration'), (SELECT id FROM funds WHERE name = 'ESG Equity Fund')),
    ((SELECT id FROM planners WHERE name = 'ESG Score Integration'), (SELECT id FROM funds WHERE name = 'Global Equity Fund'));

-- Fixed Income Analytics
INSERT INTO planner_funds (planner_id, fund_id) VALUES
    ((SELECT id FROM planners WHERE name = 'Fixed Income Analytics'), (SELECT id FROM funds WHERE name = 'Investment Grade Bond Fund')),
    ((SELECT id FROM planners WHERE name = 'Fixed Income Analytics'), (SELECT id FROM funds WHERE name = 'High Yield Bond Fund')),
    ((SELECT id FROM planners WHERE name = 'Fixed Income Analytics'), (SELECT id FROM funds WHERE name = 'Municipal Bond Fund'));

-- Target Date Fund Rebalancing
INSERT INTO planner_funds (planner_id, fund_id) VALUES
    ((SELECT id FROM planners WHERE name = 'Target Date Fund Rebalancing'), (SELECT id FROM funds WHERE name = 'Target Date 2030 Fund')),
    ((SELECT id FROM planners WHERE name = 'Target Date Fund Rebalancing'), (SELECT id FROM funds WHERE name = 'Target Date 2040 Fund')),
    ((SELECT id FROM planners WHERE name = 'Target Date Fund Rebalancing'), (SELECT id FROM funds WHERE name = 'Target Date 2050 Fund'));

-- ============================================
-- Sample Data: Planner Sources
-- ============================================

-- Daily Performance Reporting
INSERT INTO planner_sources (planner_id, source_name_id, display_order) VALUES
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM source_names WHERE name = 'Bloomberg'), 1),
    ((SELECT id FROM planners WHERE name = 'Daily Performance Reporting'), (SELECT id FROM source_names WHERE name = 'MSCI'), 2);

-- Monthly Risk Assessment
INSERT INTO planner_sources (planner_id, source_name_id, display_order) VALUES
    ((SELECT id FROM planners WHERE name = 'Monthly Risk Assessment'), (SELECT id FROM source_names WHERE name = 'MSCI'), 1),
    ((SELECT id FROM planners WHERE name = 'Monthly Risk Assessment'), (SELECT id FROM source_names WHERE name = 'FactSet'), 2);

-- ESG Score Integration
INSERT INTO planner_sources (planner_id, source_name_id, display_order) VALUES
    ((SELECT id FROM planners WHERE name = 'ESG Score Integration'), (SELECT id FROM source_names WHERE name = 'MSCI'), 1);

-- Fixed Income Analytics
INSERT INTO planner_sources (planner_id, source_name_id, display_order) VALUES
    ((SELECT id FROM planners WHERE name = 'Fixed Income Analytics'), (SELECT id FROM source_names WHERE name = 'ICE Data Services'), 1),
    ((SELECT id FROM planners WHERE name = 'Fixed Income Analytics'), (SELECT id FROM source_names WHERE name = 'Bloomberg'), 2);

-- ============================================
-- Sample Data: Planner Runs
-- ============================================

-- Daily Performance Reporting - Bloomberg Source
INSERT INTO planner_runs (planner_source_id, run_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Daily Performance Reporting' AND sn.name = 'Bloomberg' LIMIT 1), 
     (SELECT id FROM run_names WHERE name = 'Daily EOD'), 1),
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Daily Performance Reporting' AND sn.name = 'Bloomberg' LIMIT 1), 
     (SELECT id FROM run_names WHERE name = 'Post-market'), 2);

-- Monthly Risk Assessment - MSCI Source
INSERT INTO planner_runs (planner_source_id, run_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Monthly Risk Assessment' AND sn.name = 'MSCI' LIMIT 1), 
     (SELECT id FROM run_names WHERE name = 'Monthly Close'), 1);

-- Fixed Income Analytics - ICE Data Services Source
INSERT INTO planner_runs (planner_source_id, run_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Fixed Income Analytics' AND sn.name = 'ICE Data Services' LIMIT 1), 
     (SELECT id FROM run_names WHERE name = 'Daily EOD'), 1),
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Fixed Income Analytics' AND sn.name = 'ICE Data Services' LIMIT 1), 
     (SELECT id FROM run_names WHERE name = 'Intraday Real-time'), 2);

-- ============================================
-- Sample Data: Planner Reports
-- ============================================

-- Daily Performance Reporting - Bloomberg Source
INSERT INTO planner_reports (planner_source_id, report_type_id, report_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Daily Performance Reporting' AND sn.name = 'Bloomberg' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Performance'),
     (SELECT id FROM report_names WHERE name = 'Total Return Analysis'), 1),
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Daily Performance Reporting' AND sn.name = 'Bloomberg' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Holdings'),
     (SELECT id FROM report_names WHERE name = 'Current Holdings Summary'), 2);

-- Monthly Risk Assessment - MSCI Source
INSERT INTO planner_reports (planner_source_id, report_type_id, report_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Monthly Risk Assessment' AND sn.name = 'MSCI' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Risk Analysis'),
     (SELECT id FROM report_names WHERE name = 'Portfolio Risk Metrics'), 1),
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Monthly Risk Assessment' AND sn.name = 'MSCI' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Risk Analysis'),
     (SELECT id FROM report_names WHERE name = 'Value at Risk (VaR)'), 2);

-- ESG Score Integration - MSCI Source
INSERT INTO planner_reports (planner_source_id, report_type_id, report_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'ESG Score Integration' AND sn.name = 'MSCI' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Compliance'),
     (SELECT id FROM report_names WHERE name = 'ESG Compliance'), 1);

-- Fixed Income Analytics - ICE Data Services Source
INSERT INTO planner_reports (planner_source_id, report_type_id, report_name_id, display_order) VALUES
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Fixed Income Analytics' AND sn.name = 'ICE Data Services' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Portfolio Summary'),
     (SELECT id FROM report_names WHERE name = 'Cash Flow Statement'), 1),
    ((SELECT ps.id FROM planner_sources ps JOIN planners p ON ps.planner_id = p.id JOIN source_names sn ON ps.source_name_id = sn.id 
      WHERE p.name = 'Fixed Income Analytics' AND sn.name = 'ICE Data Services' LIMIT 1),
     (SELECT id FROM report_types WHERE name = 'Risk Analysis'),
     (SELECT id FROM report_names WHERE name = 'Volatility Report'), 2);
