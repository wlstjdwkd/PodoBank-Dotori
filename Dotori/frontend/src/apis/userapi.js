// 유저 정보와 관련된 API들을 모아둠 users

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9100"


//USERS
// 사용자 데이터 가져오기
export const userInfoInquiry = async (accessToken, grantType) => {
  try {
    const response = await axios.get(apiAddress+`/v1/user`, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 데이터 가져오기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 데이터 가져오기 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 생년월일 변경
export const userBirthdateChange = async (birthDate, accessToken, grantType) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/birthDate`, {birthDate:birthDate}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 생년월일 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 생년월일 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 로그아웃
export const userLogout = async (refreshToken, accessToken, grantType) => {
  try {
    console.log(refreshToken)
    console.log(accessToken)
    console.log(grantType)
    const response = await axios.patch(apiAddress+`/v1/user/logout`, {refreshToken: refreshToken}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 로그아웃 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 로그아웃 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 비밀번호 변경
  // 데이터 양식
  // {
  //   "beforePassword" : {
  //     "type" : "string"
  //   },
  //   "afterPassword" : {
  //     "type" : "string"
  //   }
  // }
export const userPasswordChange = async (pwChangeData, accessToken, grantType) => {
  console.log(pwChangeData)
  try {
    // const response = await axios.patch(apiAddress+`/v1/user/password`, pwChangeData, {
    const response = await axios.patch(apiAddress+`/v1/user/password`, {beforePassword: pwChangeData.beforePassword, afterPassword:pwChangeData.afterPassword}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 비밀번호 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 비밀번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 휴대전화 번호 변경
export const userCellPhoneNumberChange = async (phoneNumber, accessToken, grantType) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/phoneNumber`, {phoneNumber:phoneNumber}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 휴대전화 번호 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 휴대전화 번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 탈퇴하기
export const userExitDotori = async (refreshToken, accessToken, grantType) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/retire`, {refreshToken:refreshToken}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 탈퇴하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 탈퇴하기 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 이메일 인증코드 검증
export const userEmailCodeVerificate = async (email, emailVerificationCode) => {
  try {
    const response = await axios.post(apiAddress+`/v1/user/email/check-code`, {id:email, code:emailVerificationCode });
    console.log('사용자 이메일 인증코드 검증 성공:', response);
    return response;
  } catch (error) {
    console.error('사용자 이메일 인증코드 검증 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 이메일 인증코드 전송
export const userSendEmail = async (email) => {
  try {
    // const response = await axios.post(apiAddress+`/v1/user/email/check-id`, {param: {id:email}});
    const response = await axios.post(apiAddress+`/v1/user/email/check-id?id=${email}`);
    console.log('사용자 이메일 인증코드 전송 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 이메일 인증코드 전송 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 토큰 갱신
export const userTokenRefresh = async (refreshToken, accessToken, grantType) => {
  try {
    const response = await axios.post(apiAddress+`/v1/user/new-token`, {refreshToken:refreshToken}, {
      headers: {
        Authorization: `${grantType} ${accessToken}`,
      },
    });
    console.log('사용자 토큰 갱신 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 토큰 갱신 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 로그인
export const userLogin = async (loginData) => {
  try {
    const response = await axios.post(apiAddress+`/v1/user/signin`, loginData);
    console.log('사용자 로그인 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 로그인 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

// 사용자 회원가입
export const userSignup = async (signupData) => {
  // signupData.role = "ROLE_USER";
  console.log(signupData)
  try {
    const response = await axios.post(apiAddress+`/v1/user/signup`, signupData);
    console.log('사용자 회원가입 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 회원가입 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};









// // 사용자 정보 조회
// export const userInfoInquiry = async (infoInquiryData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/user`, infoInquiryData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 정보 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 정보 조회 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 회원가입
// export const userSignup = async (signupData) => {
//   signupData.role = "ROLE_USER";
//   console.log(signupData)
//   try {
//     const response = await axios.post(apiAddress+`/v1/user/signup`, signupData);
//     console.log('사용자 회원가입 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 회원가입 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 로그인
// export const userLogin = async (loginData) => {
//   try {
//     const response = await axios.post(apiAddress+`/v1/user/signin`, loginData);
//     console.log('사용자 로그인 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 로그인 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 로그아웃
// export const userLogout = async (accesToken,refreshToken, grantType) => {
//   console.log(refreshToken,'뭉',grantType)
//   try {
//     // const response = await axios.patch(apiAddress+`/v1/user/logout`, {params:{"refreshToken": refreshToken}},{
//     const response = await axios.patch(apiAddress+`/v1/user/logout`, {params: {refreshToken: refreshToken,}},{
//       headers: {
//         // Authorization: `Bearer ${refreshToken}`,
//         Authorization: `${grantType} ${accesToken}`,
//       },
//     });
//     console.log('사용자 로그아웃 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 로그아웃 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 비밀번호 변경
// export const userPasswordChange = async (pwChangeData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/user/password`, pwChangeData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 비밀번호 변경 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 비밀번호 변경 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 휴대전화 번호 변경
// export const userCellPhoneNumberChange = async (cellPhoneNumberChangeData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/user/phoneNumber`, cellPhoneNumberChangeData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 휴대전화 번호 변경 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 휴대전화 번호 변경 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 생년월일 변경
// export const userBirthdateChange = async (birthdateChangeData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/user/birthDate`, birthdateChangeData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 생년월일 변경 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 생년월일 변경 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 탈퇴하기
// export const userExitDotori = async (exitDotoriData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/user/retire`, exitDotoriData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 탈퇴하기 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 탈퇴하기 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 아이디 중복 검사
// export const userIdDuplicatedCheck = async (idDuplicatedCheckData) => {
//   try {
//     // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
//     const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData}`);
//     console.log('사용자 아이디 중복 검사 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 아이디 중복 검사 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 이메일 인증코드 전송
// export const userSendEmail = async (email) => {
//   console.log(email)
//   try {
//     // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
//     // const response = await axios.post(apiAddress+`/v1/user/email/check-id`, {param: {id:email}});
//     const response = await axios.post(apiAddress+`/v1/user/email/check-id?id=${email}`);
//     console.log('사용자 이메일 인증코드 전송 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 이메일 인증코드 전송 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 이메일 인증코드 검증
// export const userEmailCodeVerificate = async (email, emailVerificationCode) => {
//   console.log(emailVerificationCode)
//   try {
//     // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
//     // const response = await axios.post(apiAddress+`/v1/user/email/check-code`, {code:emailVerificationCode});
//     // const response = await axios.post(apiAddress+`/v1/user/email/check-code?id=${email}&code=${emailVerificationCode}`, {id:email, code:emailVerificationCode });
//     const response = await axios.post(apiAddress+`/v1/user/email/check-code?`, {id:email, code:emailVerificationCode });
//     console.log('사용자 이메일 인증코드 검증 성공:', response);
//     return response;
//   } catch (error) {
//     console.error('사용자 이메일 인증코드 검증 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };


// // reward
// // 사용자 현재 도토리 갯수 조회
// export const userDotoriValueCheck = async (dotoriValueCheckData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/reward/dotori`, dotoriValueCheckData, {
//       headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//     });
//     console.log('사용자 현재 도토리 갯수 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 현재 도토리 갯수 조회 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 현재 코인 갯수 조회
// export const userCoinValueCheck = async (coinValueCheckData, accessToken) => {
//   try {
//     const response = await axios.get(apiAddress+`/v1/reward/coin`, coinValueCheckData, {
//       headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//     });
//     console.log('사용자 현재 코인 갯수 조회 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 현재 코인 갯수 조회 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
// // 사용자 도토리를 코인전환
// export const userChangeDotoriToCoin = async (changeDotoriToCoinData, accessToken) => {
//   try {
//     const response = await axios.patch(apiAddress+`/v1/reward`, changeDotoriToCoinData, {
//       headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//     });
//     console.log('사용자 도토리를 코인전환 성공:', response.data);
//     return response;
//   } catch (error) {
//     console.error('사용자 도토리를 코인전환 실패:', error);
//     const response = error.response
//     return response
//     // throw error;
//   }
// };
