import os
import xml.etree.ElementTree as ET

def report_coverage():
    report_path = 'build/reports/kover/report.xml'
    if not os.path.exists(report_path):
        # Fallback search
        for root, dirs, files in os.walk('.'):
            if 'report.xml' in files and 'kover' in root:
                report_path = os.path.join(root, 'report.xml')
                break

    if not os.path.exists(report_path):
        print("### ❌ No Coverage Report Found")
        return

    try:
        tree = ET.parse(report_path)
        root = tree.getroot()

        metrics = {}
        for counter in root.findall('counter'):
            c_type = counter.get('type')
            missed = int(counter.get('missed', 0))
            covered = int(counter.get('covered', 0))
            total = missed + covered
            percentage = (covered / total * 100) if total > 0 else 0
            metrics[c_type] = f"{percentage:.2f}% ({covered}/{total})"

        print("\n### 📊 Code Coverage")
        print("| Metric | Coverage |")
        print("| --- | --- |")
        for m in ['INSTRUCTION', 'BRANCH', 'LINE', 'METHOD', 'CLASS']:
            if m in metrics:
                print(f"| {m.capitalize()} | {metrics[m]} |")
    except Exception as e:
        print(f"### ❌ Error parsing coverage: {e}")

if __name__ == "__main__":
    report_coverage()
