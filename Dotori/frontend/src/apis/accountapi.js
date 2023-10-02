// 계좌 정보와 관련된 API들을 모아둠 account, userauth

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9200"

// userauth
// 계좌 이름 설정
// {
//   "accountNumber" : {
//     "type" : "string"
//   },
//   "accountTitle" : {
//     "type" : "string"
//   }
// }
//1원 인증 후 계좌 이름 설정
export const accountNicknameRegist = async (nicknameRegistData, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/api/v1/oneCent/account/title`, nicknameRegistData, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계좌 이름 설정 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 이름 설정 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

// 1원 인증 전 본인확인 이메일 전송
export const accountEmailSendOneCent = async (id, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/api/v1/oneCent/own/check-id?id=${id}`, null, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('1원 인증 전 본인확인 이메일 전송 성공:', response.data);
    return response;
  } catch (error) {
    console.error('1원 인증 전 본인확인 이메일 전송 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};
// 1원 인증 전 본인확인 이메일 전송 코드 검사
// {
//   "id" : {
//     "type" : "string"
//   },
//   "code" : {
//     "type" : "string"
//   }
// }

//1원 인증 전 본인확인 이메일 전송 코드 검사 성공 -- id가 바디가 들어감 -- 기존 code에서 id추가 -- 안씀
export const accountEmailCodeVerificationOneCent = async (emailCodeVerificationOneCentData, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/api/v1/oneCent/own/check-code?id=${id}`, emailCodeVerificationOneCentData, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('1원 인증 전 본인확인 이메일 전송 코드 검사 성공:', response.data);
    return response;
  } catch (error) {
    console.error('1원 인증 전 본인확인 이메일 전송 코드 검사 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};


// // 계좌 1원 인증 번호 확인
// {
//   "bankSeq" : {
//     "type" : "integer",
//     "format" : "int64"
//   },
//   "accountNumber" : {
//     "type" : "string"
//   },
//   "verificationCode" : {
//     "type" : "string"
//   }
// }
export const accountVerificationsOnecentCheck = async (verificationsOnecentCheckData, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/api/v1/oneCent/podoBank/check-code`, verificationsOnecentCheckData, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계좌 1원 인증 번호 확인 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 1원 인증 번호 확인 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

// 계좌 1원 인증 전송
// {
//   "bankSeq" : {
//     "type" : "integer",
//     "format" : "int64"
//   },
//   "accountNumber" : {
//     "type" : "string"
//   }
// }
export const accountVerificationsOnecentSend = async (verificationsOnecentSendData, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/api/v1/oneCent/podoBank/check-account`, verificationsOnecentSendData, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('계좌 1원 인증 전송 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 1원 인증 전송 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

//Bank
// 모든 은행 정보 불러오기
export const accountWholeBank = async (accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/bank`, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('모든 은행 정보 불러오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('모든 은행 정보 불러오기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

//Account
// 전체 계좌 조회하기
export const accountWholeInquiry = async (accessToken, grantType) => {
  console.log(accessToken)
  console.log(grantType)
  try {
    const response = await axios.get(apiAddress+`/api/v1/account`, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('전체 계좌 조회하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('전체 계좌 조회하기 실패:', error.response.status, error.response.data);
    const response = error.response
    return response
    // throw error;
  }
};

// // home
// // 계좌 전체 조회
// export const accountWholeInquiry = async (wholeInquiryData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/home`, wholeInquiryData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 전체 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 전체 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계좌 은행 전체 코드 반환
// export const accountBankCodeReturn = async (bankCodeReturnData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/home/bankList`, bankCodeReturnData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 은행 전체 코드 반환 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 은행 전체 코드 반환 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };


// // verification
// // 계좌 1원 인증 전송
// export const accountVerificationsOnecentSend = async (verificationsOnecentSendData, accessToken) => {
//   try {
//     const response = await axios.post(apiAddress+`/v1/verification`, verificationsOnecentSendData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 1원 인증 전송 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 1원 인증 전송 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계좌 1원 인증 번호 확인
// export const accountVerificationsOnecentCheck = async (verificationsOnecentCheckData, accessToken) => {
//   try {
//     const response = await axios.post(apiAddress+`/v1/verification/valid`, verificationsOnecentCheckData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 1원 인증 번호 확인 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 1원 인증 번호 확인 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };


// // account
// // 계좌 전체 조회
// export const accountAllInquiry = async (allInquiryData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/account`, allInquiryData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 전체 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 전체 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계좌 이름 변경
// export const accountChangeNickname = async (changeNicknameData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/account/${changeNicknameData.accountName}`, changeNicknameData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 이름 변경 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 이름 변경 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계좌 삭제
// export const accountDelete = async (deleteData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/account/delete`, deleteData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 삭제 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 삭제 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 계좌 거래 내역 상세 조회
// export const accountTransactionInquiry = async (transactionInquiryData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/account/payment`, transactionInquiryData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('계좌 거래 내역 상세 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('계좌 거래 내역 상세 조회 실패:', error.response.status, error.response.data);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };

