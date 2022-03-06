const getMeta = (metadataContainer) => {
    const pair = (arr) => {
        return {"description": arr[0].trim(), "value": arr[1].trim()};
    };
    const m = Array
        .from(metadataContainer.children[2].children)
        .map((li) => pair(li.innerText.split('-')));
    return JSON.stringify({ "meta": m });
};
const getRating = (metadataContainer) => {
    const items = Array
        .from(document.getElementById("stud_rate").parentElement.parentElement.children)
        .map(e => e.children[1]?.textContent || e.children[0]?.textContent)
        .map(e => parseInt(e.trim()));
    const complexScore = items[0];
    const studyScore = items[1];
    const scienceScore = items[4];
    const socialScore = items[5];
    const sportScore = items[7];
    const socialActivityScore = items[6];
    
    const dataRows = metadataContainer.getElementsByTagName("li");
    const studyYear = Number.parseInt(dataRows.item(0).textContent.substring(6)) || 0;
    const isBachelor = dataRows.item(1).textContent.includes("бакалавр") || false;
    const isFirstYearBachelor = studyYear == 1 && isBachelor;

    const ratingWeight = {
        "studyScoreWeight": (isFirstYearBachelor) ? 0.8 : 0.6,
        "scienceScoreWeight": (isFirstYearBachelor) ? 0.0 : 0.2,
        "socialScoreWeight": 0.2
    }
    return JSON.stringify({
        "complexScore": complexScore || 0,
        "studyScore": studyScore || 0,
        "scienceScore": scienceScore || 0,
        "socialScore": socialScore || 0,
        "sportScore": sportScore || 0,
        "socialActivityScore": socialActivityScore || 0,
        "weights": ratingWeight
    });
};
const getSemesters = () => {
    const s = Array
        .from(document.querySelector("#bs-select-1 ul").children)
        .map((op) => { return {"id": op.value, "name": op.innerText.trim()} });
    return JSON.stringify({ "semesters": s });
};
const extractControlActivityRow = (tr) => {
    const columns = Array
        .from(tr.getElementsByTagName("td"))
        .map((el) => el.innerText.trim());
    if (columns.length == 0) {
        return {
            "type": "UNDEFINED"
        }
    } else if (columns.length == 4) {
        return {
            "type": "CONTROL_ACTIVITY",
            "name": columns[0],
            "weight": columns[1] || null,
            "weekNum": columns[2] || null,
            "markAndDate": columns[3] || null
        }
    } else if (columns[0].startsWith("Балл текущего")) {
        return {
            "type": "CURRENT_SCORE",
            "name": columns[0],
            "markAndDate": columns[1] || null
        }
    } else if (columns[0].startsWith("Контрольная неделя №")) {
        return {
            "type": "CONTROL_WEEK",
            "name": columns[0],
            "weekNum": columns[1] || null,
            "markAndDate": columns[2] || null
        }
    } else if (columns[0].startsWith("Промежуточная")) {
        return {
            "type": "INTERMEDIATE_MARK",
            "name": columns[0],
            "markAndDate": columns[1] || null,
        }
    } else if (columns[0].startsWith("Итоговая")) {
        return {
            "type": "FINAL_MARK",
            "name": columns[0],
            "markAndDate": columns[1] || null,
        }
    } else {
        return { "type": "UNDEFINED" };
    }
}
const getAllMarks = () => {
    const container = document.getElementById("div-Student_SemesterSheet__Mark");
    const rows = Array.from(container.getElementsByTagName("div"));
    const output = [];
    var i = 0;
    while (i < rows.length) {
        const e = rows[i];
        if (e.classList.contains("collapse") && i > 0) {
            const header = rows[i - 1].innerText.split(/[\(\),]/gm);
            const tableRows = Array
                .from(rows[i].getElementsByTagName("tr"))
                .map((el) => extractControlActivityRow(el));
            output.push({
                "disciplineName": header[0].trim(),
                "personName": header[1].trim(),
                "assessmentType": header[2].trim(),
                "units": header[3].trim(),
                "activities": tableRows
            });
        }
        i++;
    }
    return JSON.stringify({"payload": output});
};
metadataContainer = document.getElementById("btnErrorInfo").parentElement;

const studentNameAndGroup = metadataContainer.children[0].innerText.split(/\(.*\)/gm);
kti.onStudentNameExtracted(studentNameAndGroup[0].trim());
kti.onStudentGroupExtracted(studentNameAndGroup[1].trim());
kti.onStudentMetaExtracted(getMeta(metadataContainer));

kti.onMarksExtracted(getAllMarks());
const semesters = getSemesters();
const selectedSemesterName = 
    document.getElementsByClassName("filter-option-inner-inner")[0].innerText.trim();
kti.onSemestersExtracted(semesters, selectedSemesterName);

const rating = getRating(metadataContainer);
kti.onStudentRatingExtracted(rating);