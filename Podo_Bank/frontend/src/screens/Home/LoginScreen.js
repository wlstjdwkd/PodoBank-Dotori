import React, {useState} from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Image,
  Alert,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";
// import { useSelector, useDispatch } from 'react-redux';
// import { decrement, increment, incrementByAmount,changeNameNum } from '../../redux/slices/auth/user'

import {userLogin} from '../../apis/userapi'

export default function LoginScreen({ navigation }) {
  // const count = useSelector((state) => state.counter.count);
  // const name = useSelector((state) => state.counter.nametmp);
  // const count2 = useSelector((state) => state.whole.count2);
  // const name2 = useSelector((state) => state.whole.nameTmp2);
  // const dispatch = useDispatch();
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [isSucceed, setIsSucceed] = useState(false)
  const [loginMessage, setLoginMessage] = useState("서버 연결 실패")

 // 회원가입
 const handleUserLogin = async()=>{
  const response = await userLogin(email, password)
  if(response.status === 200){
    console.log('로그인 성공')
    setIsSucceed(true)
    setLoginMessage('로그인 성공')
  }else if(response.status === 400){
    console.log('로그인 실패')
    setLoginMessage('아이디 또는 비밀번호를 다시 확인해주세요.')
    setIsSucceed(false)
  }else{
    console.log('오류 발생')
    setLoginMessage('오류 발생')
    setIsSucceed(false)
  }
}
  return (
    <View style={styles.container}>
      {/* Logo */}
      <View style={styles.logoContainer}>
        <Image
          source={require("../../assets/images/podo_bank_name.png")}
          style={styles.logoImage}
        />
        {/* 로고 이미지 로드 */}
      </View>

      {/* ID, PW 입력창 */}
      <View style={styles.inputContainer}>
        <AntDesign name="user" size={20} style={styles.iconStyle} />
        <TextInput
          placeholder="아이디"
          style={styles.input}
          onChangeText={(text)=>{
            setEmail(text)
          }}
          keyboardType="email-address"
        />
      </View>
      <View style={styles.inputContainer}>
        <AntDesign name="lock" size={20} style={styles.iconStyle} />
        <TextInput
          placeholder="비밀번호"
          style={styles.input}
          secureTextEntry={true}
          onChangeText={(text)=>{
            setPassword(text)
          }}
        />
      </View>

      {/* 로그인 버튼 */}
      {/* 로그인 back 연동해야함 */}
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => {
          handleUserLogin()
          if(!email || !password){
            Alert.alert('입력 오류','아이디와 비밀번호를 입력해주세요.')
          }else if(isSucceed){
            navigation.navigate("HomeScreen")
          }else{
            Alert.alert('로그인 실패',loginMessage)
          }
        }}
      >
        <Text style={styles.buttonText}>로그인</Text>
      </TouchableOpacity>

      {/* 회원가입 */}
      <View style={styles.linkContainer}>
        <TouchableOpacity onPress={() => navigation.navigate("FindIDScreen")}>
          <Text style={styles.linkText}>아이디 찾기</Text>
        </TouchableOpacity>
        <Text style={styles.separator}> | </Text>
        <TouchableOpacity
          onPress={() => navigation.navigate("FindPasswordScreen")}
        >
          <Text style={styles.linkText}>비밀번호 찾기</Text>
        </TouchableOpacity>
        <Text style={styles.separator}> | </Text>
        <TouchableOpacity
          onPress={() =>
            navigation.navigate("SignupIdentityVerificationScreen")
          }
        >
          <Text style={styles.linkText}>회원가입</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
  logoImage: {
    width: 300,
    height: 300,
    resizeMode: "contain", // 'cover'나 'contain' 중 하나를 선택하여 이미지의 크기 조절 방식을 선택
  },
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "transparent",
  },
  logoContainer: {
    alignItems: "center",
    marginBottom: 80,
  },
  customButton: {
    backgroundColor: "#A175FD",
    padding: 10,
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    borderRadius: 5,
  },
  buttonText: {
    color: "white",
    fontSize: 16,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "grey",
    marginTop: 10,
    marginRight: 30,
    marginBottom: 10,
    marginLeft: 30,
    borderRadius: 5,
  },
  iconStyle: {
    marginLeft: 10,
    marginRight: 10,
  },
  input: {
    flex: 1,
    height: 40,
  },
  linkContainer: {
    flexDirection: "row",
    marginTop: 15,
    justifyContent: "center",
  },
  separator: {
    marginHorizontal: 10,
  },
});
