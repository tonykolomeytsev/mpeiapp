document.addEventListener("DOMContentLoaded", (event) => {
    try {
        let body = document.getElementsByTagName("body")[0];
        if (!dody || !body.firstChild) {
            body.innerHTML = "<div style=\"font: 16pt sans-serif;\"><p>С сожалением сообщаем, что функциональность данного раздела временно недоступна.</p><p>В ИВЦ МЭИ не хотят, чтобы студенты пользовались БАРС из приложения MpeiX. Приносим свои извинения за возможные неудобства.</p><p>Команда MpeiX</p>";
        }
    } catch (err) {
        console.log("Cannot check body emptiness")
    }
});


let getMeta = (metadataContainer) => {
    const pair = (arr) => {
        try {
            return { "description": arr[0].trim(), "value": arr[1].trim() };
        } catch (err) {
            return null;
        }
    };
    const m = Array
        .from(metadataContainer.querySelectorAll(".list-inline-item"))
        .map((li) => pair(li.innerText.split(':')))
        .filter((el) => el != null);
    return JSON.stringify({ "meta": m });
};
let getSemesters = () => {
    const s = Array
        .from(document.querySelector("#bs-select-1 ul").children)
        .map((op) => { return { "id": op.value, "name": op.innerText.trim() } });
    return JSON.stringify({ "semesters": s });
};
let extractControlActivityRow = (tr) => {
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
            "name": columns[0]
                .replace(/(\r\n|\n|\r|\t)/gm, "") // remove all newlines and tabs
                .replace(/\s{2,}/gm, " ") // remove super-spaces
                .replace(/[(]критерии[)]\s*$/gm, "") // remove dropdown menu
                .trim(),
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
let getAllMarks = () => {
    const container = document.getElementById("div-Student_SemesterSheet__Mark");
    const rows = Array.from(container.childNodes).filter((el) => el.tagName == "DIV");
    const output = [];
    var i = 0;
    while (i < rows.length) {
        const e = rows[i];
        if (e.classList.contains("collapse") && i > 0) {
            const header = rows[i - 1].innerText.split(/[,]/gm);
            const tableRows = Array
                .from(rows[i].getElementsByTagName("tr"))
                .filter((el) => !el.classList.contains("collapse"))
                .map((el) => extractControlActivityRow(el));
            output.push({
                "disciplineName": header[0].trim(),
                "personName": header[1].trim(),
                "assessmentType": header[2].trim(),
                "units": header[3]?.trim() || "",
                "activities": tableRows
            });
        }
        i++;
    }
    return JSON.stringify({ "payload": output });
};
let metadataContainer = document.querySelector("#div-FormHeader .form-row .col-sm");

let studentNameAndGroup = metadataContainer.children[0].innerText.split(/\(.*\)/gm);
kti.onStudentNameExtracted(studentNameAndGroup[0].trim());
kti.onStudentGroupExtracted(studentNameAndGroup[1].trim());
try {
    kti.onStudentMetaExtracted(getMeta(metadataContainer));
} catch (err) {
    console.log("Cannot extract metadata from the page")
}

kti.onMarksExtracted(getAllMarks());
let semesters = getSemesters();
let selectedSemesterName =
    document.getElementsByClassName("filter-option-inner-inner")[0].innerText.trim();
try {
    kti.onSemestersExtracted(semesters, selectedSemesterName);
} catch (err) {
    console.log("Cannot extract semesters from the page")
}
