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

export default function PlanManageScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  


  return (
    <View style={styles.container}>
      <Text>계좌 정보</Text>
      {/* 시민주의 통장 */}
      <View>
        <Text>시민주의 통장</Text>
      </View>
      {/* 계좌번호 */}
      <View>
        <Text>계좌번호</Text>
        <View>
          {/* 이미지 넣기 */}
          <Text>포도은행</Text>
          <Text>1235-4568-4532</Text>
        </View>
      </View>
      {/* 잔액 */}
      <View>
        <Text></Text>
        <Text></Text>
      </View>
      {/* 계획 */}
      <View>
        <Text>계획</Text>
        <View>
          <Text>시작일</Text>
          <Text>2023.09.06</Text>
        </View>
        <View>
          <Text>종료일</Text>
          <Text>2023.10.06</Text>
        </View>
      </View>
      {/* 계획중지하기 계좌 삭제하기 */}
      <View>
        <TouchableOpacity>
          <Text>계획 중지하기</Text>
        </TouchableOpacity>
        <TouchableOpacity>
          <Text>계획 삭제하기</Text>
        </TouchableOpacity>
      </View>

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // justifyContent: "center",
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
