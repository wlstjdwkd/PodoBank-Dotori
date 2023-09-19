// src/api/userApi.js
import axios from 'axios';
const apiAddress ="http://j9d107.p.ssafy.io:9000"

// // 사용 예시 
// import { useSelector, useDispatch } from 'react-redux';
// import { decrement, increment, incrementByAmount,changeNameNum } from '../redux/slices/accounts/account'
// const count = useSelector((state) => state.counter.count);
// const name = useSelector((state) => state.counter.nametmp);
// const count2 = useSelector((state) => state.whole.count2);
// const name2 = useSelector((state) => state.whole.nameTmp2);

// export const example1 = async () => {
//   const dispatch = useDispatch();
//   try {
//     const response = await axios.post(apiAddress+'/api/v1/user/register');
//     console.log(response.data);
    
//     // userRegister가 실행되면 decrement 액션을 디스패치
//     dispatch(decrement());

//     return response.data;
//   } catch (error) {
//     console.error('회원가입 서버 연결 실패:', error);
//     // throw error;
//   }
// };

export const userRegister = async (userData) => {
  try {
    const response = await axios.post(apiAddress+'/api/v1/user/register', userData);
    console.log('회원가입 서버 연결 성공:',response.data)
    return response;
  } catch (error) {
    console.error('회원가입 서버 연결 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// refresh토큰을 이용해 access token을 받음
export const userRefresh = async (refreshToken) => {
  try {
    console.log('리프레시토큰',refreshToken)
    const response = await axios.post(apiAddress+`/api/v1/user/refresh?refreshToken=${refreshToken}`);
    // const response = await axios.post(apiAddress+'/api/v1/user/refresh', null, {
    //   headers: {
    //     Authorization: `Bearer ${refreshToken}`,
    //   },
    // });
    console.log('토큰 재발급 성공:', response.data);
    console.log(response.status)
    return response;
  } catch (error) {
    console.error('토큰 재발급 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// export const userPasswordChange = async (userPassword, accessToken) => {
export const userPasswordChange = async (password, newPassword, accessToken) => {
  try {
    // const response = await axios.patch(apiAddress+'/api/v1/user/password/change', userPassword, {
    const response = await axios.patch(apiAddress+'/api/v1/user/password/change', {password:password, newPassword:newPassword}, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('비밀번호 변경 성공:',response.data)
    return response;
  } catch (error) {
    console.error('비밀번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const userLogout = async (accessToken) => {
  try {
    console.log('로그아웃중',accessToken)
    const response = await axios.post(apiAddress+'/api/v1/user/logout', null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('로그아웃 성공:',response.data)
    return response;
  } catch (error) {
    console.error('로그아웃 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// access token과 refresh token을 받음.
export const userLogin = async (email, password) => {
  try {
    const response = await axios.post(apiAddress+'/api/v1/user/login', {email:email, password:password});
    console.log('로그인 성공:',response.data)
    return response;
  } catch (error) {
    console.error('로그인 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const userEmailVerificationCheck = async (code, email, type) => {
  console.log('코드인증1',code,'and', email)
  try {
    const response = await axios.post(apiAddress+'/api/v1/user/emailVerification/check', {code:code, email:email, type:type});
    console.log('이메일 인증 코드 확인 성공:', response.data)
    return response;
  } catch (error) {
    console.error('이메일 인증 코드 확인 실패:', error.response.status);
    const response = error.response
    return response
    // throw error;
  }
};
// 이메일 인증 코드 전송 대기시간 1분 response.status 이용, 5분 이내로 인증. 10분이내로 회원가입 완료할 것.
export const userEmailVerificationSend = async (email) => {
  try {
    const response = await axios.post(apiAddress+'/api/v1/user/emailVerification', {email:email, type:"REGISTER"});
    console.log('이메일 인증 코드 전송 성공:',response.status)
    return response;
  } catch (error) {
    console.error('이메일 인증 코드 전송 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const userPWEmailVerificationSend = async (email) => {
  try {
    const response = await axios.post(apiAddress+'/api/v1/user/emailVerification', {email:email, type:"RESET_PASSWORD"});
    console.log('PW 이메일 인증 코드 전송 성공:',response.status)
    return response;
  } catch (error) {
    console.error('PW 이메일 인증 코드 전송 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 계속 연결-확인하며 회원가입간 이메일 입력시 사용, response.status 이용
export const userEmailDuplicationCheck = async (email) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/user/email/${email}`);
    console.log('아이디 중복체크 성공:',response.status)
    return response;
  } catch (error) {
    if(error.response.status === 200 || error.response.status === 409 || error.response.status === 422){
      console.log('아이디 중복 체크 실패:', error);
    }else{
      console.error('아이디 중복 체크 실패:', error);
    }
    const response = error.response
    return response
    // throw error;
  }
};
export const userWithdrawal = async (accessToken, password) => {
  console.log(accessToken)
  console.log(password)
  try {
    const response = await axios.post(apiAddress + '/api/v1/user', {password:password}, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('회원 탈퇴 성공:',response.data)
    // return response.data;
    return response;
  } catch (error) {
    console.error('회원 탈퇴 실패:', error); 
    const response = error.response
    return response
    // throw error;
  }
};
export const userInformationInquiry = async (accessToken) => {
  try {
    // const response = await axios.get(apiAddress+'/api/v1/user');
    const response = await axios.get(apiAddress + '/api/v1/user', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('회원 정보 조회 성공:',response.data)
    return response;
  } catch (error) {
    console.error('회원 정보 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 아이디 찾기
export const userIDfind = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/user/idFind');
    console.log(response.data)
    console.log('아이디 찾기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('아이디 찾기 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 비밀번호 초기화
export const userPasswordReset = async (userInfo) => {
  console.log(userInfo)
  try {
    const response = await axios.patch(apiAddress+'/api/v1/user/password/reset', userInfo);
    console.log(response.data)
    console.log('비밀번호 초기화 성공:', response.data,'and',response);
    return response;
  } catch (error) {
    console.error('비밀번호 초기화 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};


// //예시) 사용시 사용 부분에서 아래처럼 사용


// export const dataExam1 = async () => {
//   try {
//     console.log('되나')
//     const response = await axios.get('https://codingapple1.github.io/shop/data2.json');
//     console.log('굳')
//     console.log(response.data)
//     return response.data;
//   } catch (error) {
//     console.error('API 호출 실패:', error);
//     throw error;
//   }
// };
// import { dataExam1 } from '../api/userApi'

// // api를 위해서 작성했음.
// const [isLoading, setIsLoading] = useState(false);
// const [apiData, setApiData] = useState(null);
// const handleDataExam1 = async () => {
//   setIsLoading(true);
//   try {
//     const data = await dataExam1();
//     console.log(data)
//     setApiData(data);
//   } catch (error) {
//     // 에러 처리
//   } finally {
//     setIsLoading(false);
//   }
// };

// // UI 표기 {/* API 데이터 표시 */}
// {isLoading && <Text>로딩 중...</Text>}
// {apiData && (
//   <View>
//     <Text>API 데이터:</Text>
//     <Text>{JSON.stringify(apiData, null, 2)}</Text>
//   </View>
// )}