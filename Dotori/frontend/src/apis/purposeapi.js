// 목표 정보와 관련된 API들을 모아둠 purposes, category, categoryGroup

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9000"

// Purpose
// 전체 목표 조회
export const purposeList = async (listData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/purpose`, listData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('전체 목표 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('전체 목표 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 등록
export const purposeNewRegister = async (newRegisterData, accessToken) => {
  try {
    const response = await axios.post(apiAddress+`/purpose`, newRegisterData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 등록 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 등록 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 상세 조회
export const purposeDetail = async (detailData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/purpose/${detailData.purposeSeq}`, detailData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 상세 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 상세 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 완료 후 저축하기
export const purposeEndSaving = async (endSavingData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/purpose/saving`, endSavingData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 완료 후 저축하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 완료 후 저축하기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 중단하기
export const purposeQuit = async (quitData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/purpose/terminate/${quitData.purposeSeq}`, quitData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 중단하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 중단하기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 달성 현황 조회
export const purposeStatusInquiry = async (statusInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/purpose/terminate/${statusInquiryData.purposeSeq}`, statusInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 달성 현황 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 달성 현황 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};


// category
// 목표 전체 카테고리 목록 가져오기
export const purposeCategoryList = async (categoryListData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/category`, categoryListData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 전체 카테고리 목록 가져오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 전체 카테고리 목록 가져오기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 카테고리 그룹 분류
export const purposeCategoryClassify = async (categoryClassifyData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/category/classify`, categoryClassifyData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 카테고리 그룹 분류 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 카테고리 그룹 분류 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 목표 카테고리 하위 요소 가져오기
export const purposeCategoryChild = async (categoryChildData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/categoryData`, categoryChildData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 카테고리 하위 요소 가져오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 카테고리 하위 요소 가져오기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};

// CategoryGropu
// 목표 전체 카테고리 그룹 가져오기
export const purposeCategoryGroupList = async (categoryGroupListData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/categoryGroup`, categoryGroupListData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('목표 전체 카테고리 그룹 가져오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('목표 전체 카테고리 그룹 가져오기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};