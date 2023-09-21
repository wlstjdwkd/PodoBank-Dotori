// 계획 정보와 관련된 API들을 모아둠 plan, Specification

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9000"


// plan
// 계획 신규 등록
export const planNewRegister = async (newRegisterData, accessToken) => {
  try {
    const response = await axios.post(apiAddress+`/plan`, newRegisterData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 신규 등록 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 신규 등록 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 진행중인 계획 조회
export const planInProgress = async (inProgressData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/plan/${inProgressData.accountSeq}`, newRegisterData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('진행중인 계획 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('진행중인 계획 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계획 중단하기
export const planTerminate = async (terminateData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/plan/terminate/${terminateData.planSeq}`, terminateData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 중단하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 중단하기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계획 상태 변경하기
export const planStatusChange = async (statusChangeData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/plan/${statusChangeData.status}`, statusChangeData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 상태 변경하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 상태 변경하기 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계획 시작 종료 날짜 조회
export const planScheduleInquiry = async (scheduleInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/plan/${scheduleInquiryData.accountSeq}`, scheduleInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 시작 종료 날짜 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 시작 종료 날짜 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};


// Specification
// 계획 명세서 상세 조회
export const planSpecificationDetail = async (specificationDetailData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/specification/detail`, specificationDetailData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 명세서 상세 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 명세서 상세 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계획 명세서 조회
export const planSpecificationList = async (specificationListData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/specification`, specificationListData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계획 명세서 상세 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계획 명세서 상세 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};