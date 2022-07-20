readme_file = open("./README.md", "r")
lines = readme_file.readlines()
readme_file.close()

original_lines_len = len(lines)
lines[:] = lines[lines.index("---\n") + 1:]
lines_offset = original_lines_len - len(lines)

markers = []
current_marker = None
current_type = ""

for n, l in enumerate(lines):
    # new marker name definition
    if l.startswith("###"):
        current_marker = { "name": l[4:].strip() }

    # type definition encountered
    elif l.startswith("##"):
        if "FOOD" in l:
            current_type = "FOOD"
        elif "BUILDING" in l:
            current_type = "BUILDING"
        elif "HOSTEL" in l:
            current_type = "HOSTEL"
        elif "STRUCTURE" in l:
            current_type = "STRUCTURE"
        elif "OTHER" in l:
            current_type = "OTHER"
        else:
            n_pos = lines_offset + n + 1
            raise Exception("Unexpected type at line {num}: \"{line}\"".format(num = n_pos, line = l.strip()))

    # address definition
    elif "адрес:" in l.lower():
        current_marker["address"] = l[l.index(':') + 1:].strip()

    # location definition
    elif "координаты:" in l.lower():
        crds = [float(crd.strip()) for crd in l[l.index(':') + 1:].split(',')]
        current_marker["location"] = { "lat": crds[0], "lng": crds[1] }

    # uid definition
    elif "идентификатор:" in l.lower():
        current_marker["uid"] = l[l.index(':') + 1:].strip(" '`\"\n")

    elif "иконка:" in l.lower():
        current_marker["icon"] = l[l.index(':') + 1:].strip(" '`\"\n")

    elif "метка:" in l.lower():
        current_marker["tag"] = l[l.index(':') + 1:].strip()

    elif l == "\n" and current_marker is not None:
        current_marker["type"] = current_type
        markers.append(current_marker)
        current_marker = None

    elif l.startswith(">"):
        pass # just ignore as a comment

    elif l == "\n":
        pass

    else:
        n_pos = lines_offset + n + 1
        raise Exception("Unexpected symbol at line {num}: \"{line}\"".format(num = n_pos, line = l.strip()))

if current_marker is not None:
    current_marker["type"] = current_type
    markers.append(current_marker)

import json
output_file = open("markers.json", "w")
output_file.write(json.dumps(markers, ensure_ascii=False, indent=2, sort_keys=True))
output_file.close()