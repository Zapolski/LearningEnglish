
function checkTranslate(expected, actual) {
    expectedList = expected.split(" ");
    actualList = actual.split(" ");
    resultStr = '';
    var expIndex = 0;
    var actIndex = 0;
    var expStr = '';
    while (!actualList.length == 0 && !expectedList.length == 0) {
        if (expIndex < expectedList.length) {
            expStr = expectedList[expIndex];
        } else {
            expStr = "";
        }
        var actStr = actualList[actIndex];

        if (actStr == expStr) {
            resultStr = resultStr + "<span class='right'>" + actStr + " " + "</span>";//правильно написано, на своем месте
            expectedList.splice(expIndex, 1);
        } else {
            if (!(expectedList.indexOf(actStr) == -1)) {
                resultStr = resultStr + "<span class='good'>" + actStr + " " + "</span>";//присутствует в предложении (правильно написано), но не на своем месте
                expectedList.splice(actStr, 1);
            } else {
                resultStr = resultStr + "<span class='wrong'>" + actStr + " " + "</span>";//не присутствет в предложении (неправильно написно)
                expIndex++;
            }
        }
        actualList.splice(actIndex, 1);
    }
    actualList.forEach(function (entry) {
        resultStr = resultStr + "<span class='wrong'>" + entry + " " + "</span>";
    });
    return resultStr;
}

function getWithFirstUpperCase(expression) {
    return expression.charAt(0).toUpperCase() + expression.slice(1);
}

function shuffle(arr) {
    var j, temp;
    for (let i = arr.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
    return arr;
}

// function mergeArrays(...arrays) {
//     let jointArray = []

//     arrays.forEach(array => {
//         jointArray = [...jointArray, ...array]
//     })
//     const uniqueArray = jointArray.filter((item,index) => jointArray.indexOf(item) === index)
//     return uniqueArray
// }

// function mergeArrays(...arrays) {
//     let jointArray = []
//     arrays.forEach(array => {
//         jointArray = [...jointArray, ...array]
//     });
//     uniqueArray = jointArray.filter(
//         (item, index) => {
//             var count = 0;
//             for (let i = 0; i < jointArray.length; i++) {
//                 if (jointArray[i].id() === item.id()) {
//                     console.log("совпадение");
//                     count = count + 1;
//                 }
//             }
//             if (count > 1){
//                 return false;
//             } else{
//                 return true;
//             }
//         }
//     )
//     console.log(uniqueArray);
//     return uniqueArray;
// }