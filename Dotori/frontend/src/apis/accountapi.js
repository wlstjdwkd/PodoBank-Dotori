// 계좌 정보와 관련된 API들을 모아둠 home, verification, account

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9100"

// home
// 계좌 전체 조회
export const accountWholeInquiry = async (wholeInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/home`, wholeInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 전체 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 전체 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 은행 전체 코드 반환
export const accountBankCodeReturn = async (bankCodeReturnData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/home/bankList`, bankCodeReturnData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 은행 전체 코드 반환 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 은행 전체 코드 반환 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};


// verification
// 계좌 1원 인증 전송
export const accountVerificationsOnecentSend = async (verificationsOnecentSendData, accessToken) => {
  try {
    const response = await axios.post(apiAddress+`/v1/verification`, verificationsOnecentSendData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 1원 인증 전송 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 1원 인증 전송 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 1원 인증 번호 확인
export const accountVerificationsOnecentCheck = async (verificationsOnecentCheckData, accessToken) => {
  try {
    const response = await axios.post(apiAddress+`/v1/verification/valid`, verificationsOnecentCheckData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 1원 인증 번호 확인 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 1원 인증 번호 확인 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};


// account
// 계좌 전체 조회
export const accountAllInquiry = async (allInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/account`, allInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 전체 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 전체 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 이름 변경
export const accountChangeNickname = async (changeNicknameData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/account/${changeNicknameData.accountName}`, changeNicknameData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 이름 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 이름 변경 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 삭제
export const accountDelete = async (deleteData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/account/delete`, deleteData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 삭제 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 삭제 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 거래 내역 상세 조회
export const accountTransactionInquiry = async (transactionInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/account/payment`, transactionInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 거래 내역 상세 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 거래 내역 상세 조회 실패:', error.status, error.data);
    const response = error.response
    return response
    // throw error;
  }
};

