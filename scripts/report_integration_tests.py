import os
import xml.etree.ElementTree as ET

def report_integration_tests():
    total = 0
    passed = 0
    failed = 0
    skipped = 0
    failures = []

    # Integration test results are in integration-tests/build/test-results/integrationTest/
    results_dir = os.path.join('integration-tests', 'build', 'test-results', 'integrationTest')

    if not os.path.exists(results_dir):
        # Fallback to search if path is different
        for root, dirs, files in os.walk('integration-tests'):
            if 'test-results' in root and 'integrationTest' in root:
                results_dir = root
                break

    if os.path.exists(results_dir):
        for file in os.listdir(results_dir):
            if file.endswith('.xml'):
                try:
                    tree = ET.parse(os.path.join(results_dir, file))
                    root_node = tree.getroot()

                    suites = root_node.findall('testsuite') if root_node.tag == 'testsuites' else [root_node]

                    for suite in suites:
                        tests = int(suite.get('tests', 0))
                        suite_failures = int(suite.get('failures', 0))
                        suite_errors = int(suite.get('errors', 0))
                        suite_skipped = int(suite.get('skipped', 0))

                        total += tests
                        failed += (suite_failures + suite_errors)
                        skipped += suite_skipped
                        passed += (tests - suite_failures - suite_errors - suite_skipped)

                        if suite_failures > 0 or suite_errors > 0:
                            for testcase in suite.findall('testcase'):
                                failure = testcase.find('failure')
                                error = testcase.find('error')
                                if failure is not None or error is not None:
                                    name = testcase.get('name')
                                    classname = testcase.get('classname')
                                    failures.append(f"{classname}.{name}")
                except Exception:
                    pass

    print("### 🔗 Integration Test Results")
    if total == 0:
        print("⚠️ No integration test results found.")
        return

    print("| 📊 Total | ✅ Passed | ❌ Failed | ⚠️ Skipped |")
    print("| --- | --- | --- | --- |")
    print(f"| {total} | {passed} | {failed} | {skipped} |")

    if failures:
        print("\n#### ❌ Failed Integration Tests")
        for f in failures:
            print(f"- {f}")

if __name__ == "__main__":
    report_integration_tests()
