import os
import xml.etree.ElementTree as ET
from collections import defaultdict

def report_tests():
    stats = defaultdict(lambda: {'passed': 0, 'failed': 0, 'skipped': 0, 'total': 0})
    for root, dirs, files in os.walk('.'):
        for file in files:
            if file.endswith('.xml') and 'test-results' in root:
                try:
                    tree = ET.parse(os.path.join(root, file))
                    root_node = tree.getroot()
                    if root_node.tag == 'testsuites':
                        suites = root_node.findall('testsuite')
                    else:
                        suites = [root_node]

                    # Extract platform from path (e.g., build/test-results/desktopTest/TEST-...)
                    parts = root.split(os.sep)
                    platform = "Unknown"
                    if 'test-results' in parts:
                        idx = parts.index('test-results')
                        if idx + 1 < len(parts):
                            platform = parts[idx + 1]

                    for suite in suites:
                        tests = int(suite.get('tests', 0))
                        failures = int(suite.get('failures', 0))
                        errors = int(suite.get('errors', 0))
                        skipped = int(suite.get('skipped', 0))

                        stats[platform]['total'] += tests
                        stats[platform]['failed'] += (failures + errors)
                        stats[platform]['skipped'] += skipped
                        stats[platform]['passed'] += (tests - failures - errors - skipped)
                except Exception as e:
                    pass # Silently skip malformed XML

    if not stats:
        print("### ❌ No Test Results Found")
        return

    print("### 🧪 Test Results")
    print("| Platform | Total | Passed | Failed | Skipped |")
    print("| --- | --- | --- | --- | --- |")
    for platform, s in sorted(stats.items()):
        print(f"| {platform} | {s['total']} | ✅ {s['passed']} | ❌ {s['failed']} | ⚠️ {s['skipped']} |")

if __name__ == "__main__":
    report_tests()
