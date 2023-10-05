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
  Modal,
} from "react-native";
import AsyncStorage from '@react-native-async-storage/async-storage' // 스토리지에 저장하기 위해 사용되는 import
import {inputgrantType, inputAccessToken, inputRefreshToken} from "../../redux/slices/auth/user"
import {userLogin} from "../../apis/userapi"
import { useDispatch, useSelector } from "react-redux"
import { EvilIcons } from '@expo/vector-icons';
import { planStop } from "../../apis/planapi"
import { accountDelete, accountNicknameRegist, accountOneInquiry } from "../../apis/accountapi"
import { Dimensions } from "react-native";

export default function PlanManageScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const [editAccountName, setEditAccountName] = useState(false)
  const [planStopModalVisible, setPlanStopModalVisible] = useState(false)
  const [accountDeleteModalVisible, setAccountDeleteModalVisible] = useState(false)

  const [changeAccountName, setChangeAccountName] = useState(route.params.accountTitle)
  const [accountName, setAccountName] = useState(route.params.accountTitle);
  const [accountSeq, setAccountSeq] = useState(route.params.accountSeq);
  const [planSeq, setPlanSeq] = useState(route.params.planSeq);
  const [accountBalance, setAccountBalance] = useState(route.params.accountBalance);
  const [endAt, setEndAt] = useState(route.params.endAt);
  const [startedAt, setStartedAt] = useState(route.params.startedAt);
  const [accountNumber, setAccountNumber] = useState("1111111111111");
  const [bankName, setBankName] = useState('포도은행');
  
  // // 임의값
  // const [changeAccountName, setChangeAccountName] = useState("임의의계좌")
  // const [accountName, setAccountName] = useState("임의의계좌");
  // const [accountNumber, setAccountNumber] = useState("1235-4568-4532");
  // const [bankName, setBankName] = useState('포도은행');
  // const [accountSeq, setAccountSeq] = useState(2);
  // const [planSeq, setPlanSeq] = useState(3);
  // const [accountBalance, setAccountBalance] = useState(50000);
  // const [endAt, setEndAt] = useState("2023-11-11T00:00:00");
  // const [startedAt, setStartedAt] = useState("2023-09-04T00:00:00");
  

  
  const windowWidth = Dimensions.get('window').width;
  const windowHeight = Dimensions.get('window').height;

  const doPlanStop = async () => {
    try{
      const response = await planStop(planSeq, accessToken, grantType)
      if(response.status === 200){
        console.log('계획 중단 성공')
        navigation.reset({
          index: 0,
          routes: [{ name: 'MainPageScreen' }],
        });
      }else{
        console.log('계획 중단 실패', response.status)
      }
    }catch(error){
      console.log('오류 발생: 계획 중단 실패',error)
    }
  }

  const doAccountDelete = async () => {
    try{
      const response = await accountDelete(accountSeq, accessToken, grantType)
      if(response.status === 200){
        console.log('계좌 삭제 성공')
        navigation.reset({
          index: 0,
          routes: [{ name: 'MainPageScreen' }],
        });
      }else{
        console.log('계좌 삭제 실패', response.status)
      }
    }catch(error){
      console.log('오류 발생: 계좌 삭제 실패',error)
    }
  }


  const doAccountOneInquiry = async () => {
    try{
      const response = await accountOneInquiry(planSeq, accessToken, grantType)
      if(response.status === 200){
        console.log('계좌 1개 조회 성공')
        setBankName(response.data.bankName)
        setAccountNumber(response.data.accountNumber)
        setAccountBalance(response.data.accountBalance)
      }else{
        console.log('계좌 1개 조회 실패', response.status)
      }
    }catch(error){
      console.log('오류 발생: 계좌 1개 조회 실패',error)
    }
  }

  const handleAccountNameChange = () => {
    doAccountNameChange()
    setEditAccountName(false)

  }
  const doAccountNameChange = async () => {
    const nicknameRegistData = {
      accountNumber:accountNumber,
      accountTitle:changeAccountName,
    }
    try{
      const response = await accountNicknameRegist(nicknameRegistData, accessToken, grantType)
      if(response.status === 200){
        console.log('계좌 이름 변경 성공')
        setAccountName(changeAccountName)
      }else{
        console.log('계좌 이름 변경 실패', response.status)
      }
    }catch(error){
      console.log('오류 발생: 계좌 이름 변경 실패',error)
    }
  }


  useEffect(()=>{
    doAccountOneInquiry()
  },[])

  return (
    <View style={styles.container}>
      <View style={{alignSelf:'center', flex:0.1}}>
        <Text style={styles.headerTitle}>계좌 정보</Text>
      </View>

      {/* 시민주의 통장 */}
      <View style={[{flexDirection:'row', flex:0.1,}]}>
        { editAccountName
          ?
          <TextInput
            style={[styles.nameChangeInput,{width: windowWidth*0.5,}]}
            value = {changeAccountName}
            returnKeyType="done"
            onSubmitEditing={()=>{
              handleAccountNameChange()
            }}
            onChangeText={(text)=>{
              setChangeAccountName(text)
            }}
          />
          :
          <Text style={styles.subTitle1}>{accountName}</Text>
        }
        { editAccountName
          ? (<TouchableOpacity
              onPress={()=>{
                handleAccountNameChange()
              }}
              >
              <Text style={styles.nameChangeButton}>수정</Text>
            </TouchableOpacity>)
          
          : (<TouchableOpacity
            onPress={()=>{
                setEditAccountName(true)
              }}
            >
              <EvilIcons name="pencil" size={30} color="black" />
            </TouchableOpacity>)
          
        }
      </View>
      <View style={[styles.divideLine,{}]}></View>

      {/* 계좌번호 */}
      <View style={{flex:0.2, justifyContent:"space-around"}}>
        <Text style={[styles.subTitle2,{}]}>계좌번호</Text>
        <View style={{flexDirection:'row', justifyContent:"space-between"}}>
          {/* 이미지 넣기 */}
          <Image
            style={[styles.podoBankImage,{}]}
            source={require("../../assets/images/logo_podo.png")}
          />
          <View style={{justifyContent:"space-between"}}>
            <Text style={[styles.textRight,{}]}>{bankName}</Text>
            <Text style={[styles.textRight,{}]}>{accountNumber.slice(0,4)}-{accountNumber.slice(4,6)}-{accountNumber.slice(6)}</Text>
          </View>
        </View>
      </View>
      <View style={[styles.divideLine,{}]}></View>

      {/* 잔액 */}
      <View style={{flex:0.2, justifyContent:"space-around"}}>
        <Text style={[styles.subTitle2,{}]}>잔액</Text>
        <Text style={[styles.textRight,{}]}>{accountBalance != null ? accountBalance.toLocaleString() : 0}원</Text>
      </View>
      <View style={[styles.divideLine,{}]}></View>

      {/* 계획 */}
      <View style={{flex:0.2, justifyContent:"space-around", }}>
        <Text style={[styles.subTitle2,{}]}>계획</Text>
        <View style={{}}>
          <View style={[styles.dayText,{}]}>
            <Text style={[styles.normalText,{}]}>시작일</Text>
            <Text style={[styles.normalText,{}]}>{startedAt.slice(0,4)}.{startedAt.slice(5,7)}.{startedAt.slice(8,10)}</Text>
          </View>
          <View style={[styles.dayText,{}]}>
            <Text style={[styles.normalText,{}]}>종료일</Text>
            <Text style={[styles.normalText,{}]}>{endAt.slice(0,4)}.{endAt.slice(5,7)}.{endAt.slice(8,10)}</Text>
          </View>
        </View>
      </View>

      {/* 계획중지하기 계좌 삭제하기 */}
      <View style={[styles.planManage,{}]}>
        <TouchableOpacity
          onPress={()=>{
            setPlanStopModalVisible(true)
          }}
          >
          <Text style={[styles.stopText,{}]}>계획 중지하기</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={()=>{
            setAccountDeleteModalVisible(true)
          }}>
        <Text style={[styles.stopText,{}]}>계좌 삭제하기</Text>
        </TouchableOpacity>
      </View>

      { (planStopModalVisible || accountDeleteModalVisible)
        ?null
        :<View style={styles.centeredView}></View>
      }
      {/* 계획 중지 모달창 */}
      { planStopModalVisible
        ?
        (<View style={styles.centeredView}>
          <Modal
            animationType="none"
            transparent={true}
            visible={planStopModalVisible}
            onRequestClose={() => {
              setPlanStopModalVisible(false);
            }}>
            <View style={styles.centeredView}>
              <View style={styles.modalView}>
                <Text style={styles.modalText}>현재 계획을 중지하시겠습니까?</Text>
                <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      doPlanStop()
                      setPlanStopModalVisible(false)
                    }}>
                    <Text style={styles.textStyle}>예</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      setPlanStopModalVisible(false)
                    }}>
                    <Text style={styles.textStyle}>아니오</Text>
                  </TouchableOpacity>
                </View>
              </View>
            </View>
          </Modal>
        </View>)
        : null
      }
      {/* 계좌 삭제 모달창 */}
      { accountDeleteModalVisible
        ?
        (<View style={[styles.centeredView]}>
          <Modal
            animationType="none"
            transparent={true}
            visible={accountDeleteModalVisible}
            onRequestClose={() => {
              setAccountDeleteModalVisible(false);
            }}>
            <View style={styles.centeredView}>
              <View style={styles.modalView}>
                <Text style={styles.modalText}>연동된 계좌를 삭제하시겠습니까?</Text>
                <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      doAccountDelete()
                      setAccountDeleteModalVisible(false)
                    }}>
                    <Text style={styles.textStyle}>예</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[styles.button, styles.buttonClose]}
                    onPress={() => {
                      setAccountDeleteModalVisible(false)
                    }}>
                    <Text style={styles.textStyle}>아니오</Text>
                  </TouchableOpacity>
                </View>
              </View>
            </View>
          </Modal>
        </View>)
        : null
      }

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    padding: 45,
    backgroundColor: "white",
  },
  // 제작
  headerTitle: {
    fontSize: 18,
    fontWeight: "bold",
  },
  subTitle1: {
    fontWeight: "bold",
    fontSize: 23,
  },
  textRight:{
    textAlign: "right",
    fontSize:16,
  },
  normalText:{
    fontSize:16,
  },
  podoBankImage: {
    width: 50,
    height: 50,
  },
  subTitle2:{
    fontSize: 20, 
    fontWeight:"bold"
  },
  divideLine:{
    height:5, 
    backgroundColor:"#F5F5F5"
  },
  stopText:{
    fontWeight:'bold', 
    color:'gray',
  },
  planManage:{
    flex:0.2, 
    flexDirection:"row", 
    justifyContent:"space-between", 
    alignItems:"flex-end"
  },
  dayText:{
    flexDirection:"row", 
    justifyContent:"space-between",
    marginVertical:10
  },
  nameChangeButton:{
    height:40, 
    borderWidth:1, 
    color:"white", 
    backgroundColor:"#FF965C", 
    borderColor:'#FF965C', 
    textAlignVertical:'center', 
    marginHorizontal:10, 
    borderRadius: 10, 
    padding: 10
  },
  nameChangeInput:{
    borderWidth:1, 
    borderColor:'black', 
    height:40,  
    padding:10, 
    borderRadius: 10
  },
  // 모달
  // 모달 관련 스타일
  centeredView: {
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  modalView: {
    // margin: 20,
    top: '225%',
    // left: '50%',
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    // alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  button: {
    flex:1,
    borderRadius: 20,
    padding: 10,
    marginHorizontal: 10,
    elevation: 2,
  },
  buttonOpen: {
    backgroundColor: '#F194FF',
  },
  buttonClose: {
    // backgroundColor: '#2196F3',
    backgroundColor: '#FF965C',
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
  },
});
