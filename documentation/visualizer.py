import matplotlib.pyplot as plt
import os

IN_FILE = 'paper.tex'

# Subject Name, Offset
SUBJECT_OFFSET_PAIRS = [
    ('Hard Drive 100MB chunks write', 5),
    ('Memory Sort', 6),
    ('MySQL 10k Insert', 7),
    ('Redis 10k Insert', 8),
    ('Redis 10k Delete', 9),
]

def init():
    plt.rcParams["figure.autolayout"] = True
    os.makedirs('images_generated', exist_ok=True)

def read_lines(in_file: str) -> [str]:
    lines = []
    with open(in_file) as f:
        lines = f.read().split('\n')
    return lines

def get_value_error(line: str) -> (float, float):
    parts = line.split(' & ')[3].replace('\\', '').split(' &$pm$ ')
    [value, error] = [float(x.strip().replace(',', '')) for x in parts]
    return value, error

def main():
    init()
    lines = read_lines(IN_FILE)

    # Caption-Subject-Value-Error map
    # caption: { subject: [(value, error)] }
    csve_map = {}

    # Build csve
    for i in range(0, len(lines)):
        if '\caption{' not in lines[i]:
            continue
        caption = lines[i].split('\caption{')[1].split('}')[0].replace(', ', '\n')

        for subject, offset in SUBJECT_OFFSET_PAIRS:
            value, error = get_value_error(lines[i + offset])

            if caption not in csve_map:
                csve_map[caption] = {}
            if subject not in csve_map[caption]:
                csve_map[caption][subject] = []

            csve_map[caption][subject].append((value, error))
    
    # Horizontal bar graph, comparison across different captions, subject-by-subject, one graph per subject
    for subject, _ in SUBJECT_OFFSET_PAIRS:
        for caption in csve_map:
            values = [x[0] for x in csve_map[caption][subject]]
            errors = [x[1] for x in csve_map[caption][subject]]
            bars = plt.barh(caption, values, xerr=errors)
            plt.bar_label(bars)
        
        plt.title(subject)
        plt.ylabel('Subject')
        plt.xlabel('Time (ms/op), lower is better')
        plt.savefig(f'images_generated/{subject}.png')
        plt.clf()

    # Compare hard_drive_values across all tests
    # accurate number of bytes to swapped out


if __name__ == '__main__':
    main()

