import React, { useEffect, useRef, useState, } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
  StyleSheet,
  Alert,
  Linking,
} from "react-native";
import AsyncStorage from '@react-native-async-storage/async-storage' // 스토리지에 저장하기 위해 사용되는 import
import {inputgrantType, inputAccessToken, inputRefreshToken} from "../../redux/slices/auth/user"
import {userLogin} from "../../apis/userapi"
import { useDispatch, useSelector } from "react-redux" 

export default function LoginScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const idRef = useRef(null)
  const passwordRef = useRef(null)
  const [idSave, setIdSave] = useState(false) // 아이디 저장여부
  const [emailValue, setEmailValue] = useState("")
  const [passwordValue, setPasswordValue] = useState("")
  const [loginMessage, setLoginMessage] = useState("")

  const handleLogin = () => {
    console.log(emailValue+'and'+passwordValue)
    if (!emailValue) {
      setLoginMessage("이메일을 입력해주세요.");
      idRef.current.focus()
    } else if (!passwordValue) {
      setLoginMessage("비밀번호를 입력해주세요.");
      passwordRef.current.focus()
    } else {
      console.log('로그인하기');
      doLogin();
    }
  }

  const doLogin = async () => {
    const data = {id:emailValue, password:passwordValue}
    try{
      const response = await userLogin(data)
      if(response.status === 200){
        console.log('로그인 성공')
        dispatch(inputgrantType(response.data.grantType))
        dispatch(inputAccessToken(response.data.accessToken))
        dispatch(inputRefreshToken(response.data.refreshToken))
        try{
          if(idSave){
            await AsyncStorage.setItem("id", emailValue);
            console.log(emailValue)
          }else{
            await AsyncStorage.removeItem('id')
          }
          navigation.reset({
            index: 0,
            routes: [{ name: 'MainPageScreen' }],
          });
        }catch(error){
          setLoginMessage('오류 발생: 로그인 실패')
        }
      }else if(response.status === 404){
        console.log('로그인 실패')
        Alert.alert('로그인 실패', "아이디와 비밀번호를 확인해주세요.")
        setLoginMessage("아이디와 비밀번호를 확인해주세요.")
      }else{
        setLoginMessage('오류 발생: 로그인 실패')
      }
    }catch(error){
      console.log(error)
      setLoginMessage('오류 발생: 로그인 실패')
    }
  }

  useEffect(()=>{
    dispatch(inputgrantType(null))
    dispatch(inputAccessToken(null))
    dispatch(inputRefreshToken(null))

    const getExistingId = async () => {
      try{
        const existingId = await AsyncStorage.getItem("id");
        if(existingId){
          // 데이터 발견, 해당 데이터로 무언가 수행
          setEmailValue(existingId)
          passwordRef.current.focus()
          setIdSave(true)
        }else {
          // 데이터 없음
          idRef.current.focus()
        }
      }catch (error) {
        console.log(error)
      }
    }
    getExistingId()
  }, [])

  // // 카카오 로그인
  // const handleKakaoLogin = () => {
  //   const kakaoLoginUrl = 'https://accounts.kakao.com/login/?continue=https%3A%2F%2Fkauth.kakao.com%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26redirect_uri%3Dhttp%253A%252F%252Fj9d107.p.ssafy.io%253A9100%252Fv1%252Fkakao%252Fcallback%26through_account%3Dtrue%26client_id%3D866dc3358fdb2e42441c258a45fe9850#login';
  //   Linking.openURL(kakaoLoginUrl)
  //     .then((url) => {
  //       console.log('URL opened:', url);
  //     })
  //     .catch((error) => {
  //       console.log('URL open error:', error);
  //     });
  // };

  return (
    <View style={styles.container}>
      <Image
        style={styles.image}
        source={require("../../assets/images/dotori_logo.png")}
      />

      <TextInput
        style={styles.input}
        placeholder="이메일" 
        keyboardType="email-address"
        returnKeyType ="next"
        ref = {idRef}
        value = {emailValue}
        onChangeText={(text)=>{
          setEmailValue(text)
        }}
        onSubmitEditing={()=>{
          passwordRef.current.focus()
        }}
      />
      <TextInput 
        style={styles.input} 
        placeholder="비밀번호" 
        keyboardType="default"
        returnKeyType ="done"
        secureTextEntry={true}
        ref = {passwordRef}
        value= {passwordValue}
        onChangeText={(text)=>{
          setPasswordValue(text)
        }}
        onSubmitEditing={()=>{
          handleLogin()
        }}
      />

      <TouchableOpacity
        style={styles.idSave}
        onPress={()=>{
          setIdSave(!idSave)
        }}
      >
        <View style={styles.checkboxContainer}>
          <View style={[styles.checkbox]}>
            {idSave
            ?(<Text style={{textAlign:'center'}}>✔️</Text>)
            :null}
          </View>
          {/* <View style={[styles.checkbox, {backgroundColor:idSave?'black':'white'}]}></View> */}
          <Text style={{ color: "#878787" }}>아이디저장</Text>
        </View>
      </TouchableOpacity>
      <View style={styles.referenceMessage}>
        <Text style={[styles.referenceMessageText, ]}>{loginMessage}</Text>
      </View>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          handleLogin()
        }}
      >
        <Text style={styles.buttonText}>로그인</Text>
      </TouchableOpacity>

      <View style={styles.linksContainer}>
        <TouchableOpacity
          onPress={()=>{
            Alert.alert('', '아이디 찾기 구현 필요')
            navigation.navigate("LoginScreen")
          }}
        >
          <Text style={styles.linkText}>아이디 찾기</Text>
        </TouchableOpacity>

        <Text style={styles.linkText}>|</Text>
        <TouchableOpacity
          onPress={()=>{
            Alert.alert('', '비밀번호 찾기 구현 필요')
            navigation.navigate("LoginScreen")
          }}
        >
          <Text style={styles.linkText}>비밀번호 찾기</Text>
        </TouchableOpacity>

        <Text style={styles.linkText}>|</Text>
        <TouchableOpacity onPress={() => {
          navigation.navigate("SignUp1Screen")
        }}>
          <Text style={styles.linkText}>회원가입</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.divider}>
        <Text style={styles.dividerText}>SNS 계정으로 로그인</Text>
      </View>

      {/* 카카오, 네이버 로그인 버튼은 라이브러리나 직접 이미지로 구현해야 합니다. */}
      <View style={styles.oauth}>
        <TouchableOpacity
          onPress={()=>{
            // handleKakaoLogin()
          }}
        >
          <Image
            style={styles.oauthImage}
            source={require("../../assets/images/kakao.png")}
          />
        </TouchableOpacity>
        <TouchableOpacity>
          <Image
            style={styles.oauthImage}
            source={require("../../assets/images/naver.png")}
          />
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 45,
    backgroundColor: "white",
  },
  image: {
    width: 200,
    height: 80,
    marginBottom: 60,
  },
  oauth: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    marginBottom: 20,
  },
  input: {
    width: "100%",
    height: 40,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
  },
  checkboxContainer: {
    flexDirection: "row",
    alignItems: "center",
    alignSelf: "flex-start", // 추가: 왼쪽 정렬
    // marginBottom: 30,
  },
  checkbox: {
    width: 20,
    height: 20,
    borderRadius: 5,
    backgroundColor: "white",
    borderWidth: 1,
    marginRight: 10,
  },
  oauthImage: {
    width: 50,
    height: 50,
    marginLeft: 10,
    marginRight: 10,
  },
  button: {
    width: "100%",
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 10,
  },
  buttonText: {
    color: "#fff",
  },
  linksContainer: {
    flexDirection: "row",
    justifyContent: "space-between", // 수정: 양쪽 끝으로 확장
    width: "100%",
    marginBottom: 60,
    paddingHorizontal: 10,
  },
  divider: {
    width: "100%",
    height: 1,
    backgroundColor: "#BAC0CA",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    marginBottom: 30, // 추가: 여백
  },
  dividerText: {
    position: "absolute", // 중앙에 배치하기 위해
    backgroundColor: "white", // 배경색으로 구분선 가리기
    paddingHorizontal: 10, // 좌우 패딩
    color: "#858585",
  },
  linkText: {
    color: "#858585",
  },
  idSave:{
    width:"100%", 
    // marginBottom: 30,
  },
  referenceMessage:{
    marginVertical: 10,
  },
  referenceMessageText:{
    color: 'red',
  }
});
