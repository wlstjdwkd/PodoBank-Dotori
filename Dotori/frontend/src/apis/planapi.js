// 계획 정보와 관련된 API들을 모아둠 plan, chatgpt, category, categoryGroup, categorydata

import axios from "axios";
// apiAddress는 수정 필요
const apiAddress = "http://j9d107.p.ssafy.io:9200";

// PLAN
// 계획 종료 후 저축하기
// {
//   "planSeq" : {
//     "type" : "integer",
//     "format" : "int64"
//   },
//   "purposeSavingList" : {
//     "type" : "array",
//     "items" : {
//       "$ref" : "#/components/schemas/PurposeSavingDTO"
//     }
//   },
//   "totalSaving" : {
//     "type" : "number"
//   }
// }
export const planSaving = async (savingData, accessToken, grantType) => {
  try {
    const response = await axios.post(
      apiAddress + `/api/v1/plan/saving`,
      savingData,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("계획 종료 후 저축하기 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 종료 후 저축하기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};
// 계획 종료 후 저축안하고 끝내기
export const planNoSaving = async (planSeq, accessToken, grantType) => {
  try {
    const response = await axios.patch(
      apiAddress + `/api/v1/plan/completed/${planSeq}`,
      null,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("계획 종료 후 저축안하고 끝내기 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 종료 후 저축안하고 끝내기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};
// 계획 중단하기
export const planStop = async (planSeq, accessToken, grantType) => {
  try {
    const response = await axios.patch(
      apiAddress + `/api/v1/plan/stop/${planSeq}`,
      null,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("계획 중단하기 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 중단하기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

// 계획 신규 등록
// {
//   "accountSeq" : {
//     "type" : "integer",
//     "format" : "int64"
//   },
//   "startedAt" : {
//     "type" : "string"
//   },
//   "endAt" : {
//     "type" : "string"
//   },
//   "categoryGroupList" : {
//     "type" : "array",
//     "items" : {
//       "$ref" : "#/components/schemas/CategoryGroupListDTO"
//     }
//   }
// }
export const planNewRegister = async (
  newRegisterData,
  accessToken,
  grantType
) => {
  try {
    console.log("accessToken: " + accessToken + " " + grantType);
    console.log("newRegisterData: " + newRegisterData);
    const response = await axios.post(
      apiAddress + `/api/v1/plan`,
      newRegisterData,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("계획 신규 등록 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 신규 등록 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

// CHATGPT
// 계획 GPt이용 카테고리 분류하기
// {
//   "categoryGroups" : {
//     "type" : "array",
//     "items" : {
//       "type" : "string"
//     }
//   },
//   "categorise" : {
//     "type" : "array",
//     "items" : {
//       "$ref" : "#/components/schemas/CategoryDTO"
//     }
//   }
// }
export const planClassifyChatGpt = async (
  classifyChatGptData,
  accessToken,
  grantType
) => {
  try {
    const response = await axios.post(
      apiAddress + `/api/v1/chatgpt`,
      classifyChatGptData,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("계획 전체 카테고리 그룹 가져오기 성공:", response.data);
    // console.log("123", response.data[0].categories);
    return response;
  } catch (error) {
    console.error(
      "계획 전체 카테고리 그룹 가져오기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

// categoryGroup
// 계획 전체 카테고리 그룹 가져오기
export const planCategoryGroupList = async (accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress + `/api/v1/categoryGroup`, null, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log("계획 전체 카테고리 그룹 가져오기 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 전체 카테고리 그룹 가져오기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};


// categorydata
// 계획 카테고리 사용처 목록 가져오기
// [
//   {
//     "dataCode": "string",
//     "dataName": "string",
//     "count": 0
//   }
// ]
export const planCategoryUsingSpot = async (categorySeq, accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/categoryData?categorySeq=${categorySeq}`,  {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계획 카테고리 사용처 목록 가져오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 카테고리 사용처 목록 가져오기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};
export const planCategoryDeleteSpot = async (dataCode, accessToken, grantType) => {
  try {
    const response = await axios.delete(apiAddress+`/api/v1/categoryData`, {dataCode:dataCode}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계획 카테고리 사용처 목록 제거하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 카테고리 사용처 목록 제거하기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};




// category
// 계획 전체 카테고리 목록 가져오기
export const planCategoryList = async (accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/category`, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log("계획 전체 카테고리 목록 가져오기 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "계획 전체 카테고리 목록 가져오기 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

// plan
// // 계획 신규 등록
// export const planNewRegister = async (newRegisterData, accessToken) => {
//   try {
//     const response = await axios.post(apiAddress+`/v1/plan`, newRegisterData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 신규 등록 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 신규 등록 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// 진행중인 계획 조회
export const planInProgress = async (
  inProgressData,
  accessToken,
  grantType
) => {
  try {
    console.log(inProgressData);
    console.log(accessToken + " " + grantType);
    const response = await axios.get(
      apiAddress + `/api/v1/plan/${inProgressData}`,
      // newRegisterData,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("진행중인 계획 조회 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "진행중인 계획 조회 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

// 미분류 항목 조회
export const unclassifiedList = async (
  inProgressData,
  accessToken,
  grantType
) => {
  try {
    console.log(inProgressData);
    console.log(accessToken + " " + grantType);
    const response = await axios.get(
      apiAddress + `/api/v1/payment/${inProgressData}`,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("진행중인 계획 조회 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "진행중인 계획 조회 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

//미분류 항목 업데이트
export const unClassifiedUpdate = async (
  inProgressData,
  planSeq,
  accessToken,
  grantType
) => {
  try {
    console.log(inProgressData);
    console.log(accessToken + " " + grantType);
    const response = await axios.patch(
      apiAddress + `/api/v1/payment/${planSeq}`,
      inProgressData,
      {
        headers: {
          Authorization: `${grantType} ${accessToken}`,
        },
      }
    );
    console.log("진행중인 계획 조회 성공:", response.data);
    return response;
  } catch (error) {
    console.error(
      "진행중인 계획 조회 실패:",
      error.response.status,
      error.response.data
    );
    const response = error.response;
    return response;
    // throw error;
  }
};

//명세서 전체 조회하기
export const planSpecificationList = async (accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/v1/plan/specification`,  {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계획 명세서 전체 조회하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 명세서 전체 조회하기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

//명세서 상세 조회하기
export const planSpecificationDetail = async (planSeq, accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/v1/planDetail/specification?planSeq=${planSeq}`,  {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계획 명세서 상세 조회하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 명세서 상세 조회하기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

// // 계획 중단하기
// export const planTerminate = async (terminateData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/plan/terminate/${terminateData.planSeq}`, terminateData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 중단하기 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 중단하기 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계획 상태 변경하기
// export const planStatusChange = async (statusChangeData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/plan/${statusChangeData.status}`, statusChangeData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 상태 변경하기 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 상태 변경하기 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계획 시작 종료 날짜 조회
// export const planScheduleInquiry = async (scheduleInquiryData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/plan/${scheduleInquiryData.accountSeq}`, scheduleInquiryData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 시작 종료 날짜 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 시작 종료 날짜 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };

// // Specification
// // 계획 명세서 상세 조회
// export const planSpecificationDetail = async (specificationDetailData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/specification/detail`, specificationDetailData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 명세서 상세 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 명세서 상세 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계획 명세서 조회
// export const planSpecificationList = async (specificationListData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/specification`, specificationListData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계획 명세서 상세 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계획 명세서 상세 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };