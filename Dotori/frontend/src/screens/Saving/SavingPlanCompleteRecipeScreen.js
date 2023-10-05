import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  ScrollView,
  Modal,
} from "react-native";

import { Audio } from 'expo-av';
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { planSpecificationDetail, planNoSaving } from "../../apis/planapi"

export default function SavingPlanCompleteRecipeScreen({ route, navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  
  // route.params에서 선택한 계좌와 명세서 번호(receipeSeq)를 가져옴
  const [accountName, setAccountName] = useState(route.params.accountName)
  const [planSeq, setPlanSeq] = useState(route.params.planSeq)
  // const [accountSeq, setAccountSeq] = useState(route.params.accountSeq)
  const [accountSeq, setAccountSeq] = useState(2)

  // 가상 데이터 - 명세서 항목들
  const [receipeItems, setReceipeItems] = useState([])
  // const [receipeItems, setReceipeItems] = useState([
  //   { categoryTitle: "식비", expense: 50000, savings: 20000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "어디까지올라가는거에요", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   { categoryTitle: "주거", expense: 30000, savings: 10000 },
  //   // 다른 항목들 추가
  // ])

  // 추가 저축 항목
  const [additionalSavings, setAdditionalSavings] = useState(null)

  const [totalExpense, setTotalExpense] = useState(null)
  const [totalSavings, setTotalSavings] = useState(null)
  const [noSavingModalVisible, setNoSavingModalVisible] = useState(false)
  const [currentAccountAmount, setCurrentAccountAmount] = useState(null)
  const [sound, setSound] = useState();


  const playSound = async() => {
    console.log('Loading Sound');
    const { sound } = await Audio.Sound.createAsync( require('../../assets/nextPage.mp3')
    );
    setSound(sound);

    console.log('Playing Sound');
    await sound.playAsync();

  }
  
  // 총계 계산
  const funcTotalExpense = (counting) => {
    return counting.reduce(
      (acc, item) => acc + item.expense,
      0
    )
  }
  const funcTotalSavings = (counting) => {
    return counting.reduce(
      (acc, item) => acc + item.savings,
      0
    )
  }

  const doPlanSpecificationDetail = async () =>{
    try{
      const response = await planSpecificationDetail(planSeq, accessToken, grantType)
      if(response.status === 200){
        setReceipeItems(response.data.planDetailList)
        setAdditionalSavings(response.data.additionalSaving)
        setTotalExpense(funcTotalExpense(response.data.planDetailList))
        setTotalSavings(funcTotalSavings(response.data.planDetailList))
        setCurrentAccountAmount(response.data.currentAmount) // 계좌 잔액
        console.log("명세서 상세 조회하기 성공", response.data)
      }else{
        console.log("명세서 상세 조회하기 실패", response.status)
      }
    }catch(error){
      console.log("오류 발생 명세서 상세 조회하기 실패: ", error)
    }
  }

  const handlePlanNoSaving = async() =>{
    await doPlanNoSaving()
    setNoSavingModalVisible(false)
  }
  const doPlanNoSaving = async () => {
    try{
      const response = await planNoSaving(planSeq, accessToken, grantType)
      if(response.status === 200){
        console.log("저축 안하기 성공", response.data)
        navigation.reset({
          index: 0,
          routes: [{ name: 'MainPageScreen' }],
        });
      }else{
        console.log("저축 안하기 실패", response.status)
      }
    }catch(error){
      console.log()
    }
  }

  useEffect(() => {
    return sound
      ? () => {
          console.log('Unloading Sound');
          sound.unloadAsync();
        }
      : undefined;
  }, [sound]);

  useEffect(()=>{
    doPlanSpecificationDetail()
    // setTotalExpense(funcTotalExpense(receipeItems))
    // setTotalSavings(funcTotalSavings(receipeItems))
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="명세서 보기"
        navigation={navigation}
        cancelNavi="MainPageScreen"
      ></HeaderComponent>

      {/* 선택한 명세서를 화면에 표시 */}
      <ScrollView contentContainerStyle={styles.contentContainer}>
        {/* 명세서 표 */}
        <View style={styles.receipeTable}>
          {/* 계좌 별칭 */}
          <View style={styles.topContainer}>
            <Text style={styles.label}>{accountName}</Text>
            <Text style={styles.amount}>
              {additionalSavings!=null
                ? (totalSavings + additionalSavings)?(totalSavings + additionalSavings).toLocaleString():(totalSavings + additionalSavings)
                : totalSavings?totalSavings.toLocaleString():totalSavings
              }원
              
            </Text>
          </View>

          {/* 제목 행 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}>카테고리</Text>
            <Text style={styles.tableCell}>지출</Text>
            <Text style={styles.tableCell}>저축</Text>
          </View>

          {/* 구분선 */}
          <View style={styles.tableLine} />

          {/* 항목들 */}
          {receipeItems.map((item, index) => (
            <View style={styles.tableRow} key={index}>
              <Text style={styles.tableIndexCell}>{index + 1}</Text>
              <Text style={styles.tableCell}>{item.categoryTitle}</Text>
              <Text style={styles.tableCell}>
                {item.expense.toLocaleString()}
              </Text>
              <Text style={styles.tableCell}>
                {item.savings.toLocaleString()}
              </Text>
            </View>
          ))}

          {/* 구분선 */}
          <View style={styles.tableLine} />

          {/* 사용 합계 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}></Text>
            <Text style={styles.tableCell}>
              {totalExpense ? totalExpense.toLocaleString() : totalExpense}
            </Text>
            <Text style={styles.tableCell}>
              {totalSavings ? totalSavings.toLocaleString() : totalSavings}
            </Text>
          </View>

          {/* 추가 저축 항목 */}
          {additionalSavings!=null
            ?
            (<View style={styles.tableRow}>
              <Text style={styles.tableIndexCell}></Text>
              <Text style={styles.tableCell}>추가 저축</Text>
              <Text style={styles.tableCell}></Text>
              <Text style={styles.tableCell}>
                {additionalSavings ? additionalSavings.toLocaleString() : additionalSavings}
              </Text>
            </View>)
            : null
          }

          {/* 구분선 */}
          {additionalSavings!=null
            ?
            <View style={styles.tableDotLine} />
            :null
          }

          {/* 총계 행 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}>총계</Text>
            <Text style={styles.tableCell}>
              {totalExpense?totalExpense.toLocaleString():totalExpense}
            </Text>
            <Text style={styles.tableCell}>
              {additionalSavings
                ?(totalSavings + additionalSavings)?(totalSavings + additionalSavings).toLocaleString():(totalSavings + additionalSavings)
                :totalSavings?totalSavings.toLocaleString():totalSavings
              }
            </Text>
          </View>
          <View style={styles.tableLine} />
        </View>

        <View style={{width:"100%", margin:50, flexDirection:"row", justifyContent: "space-evenly"}}>
          <TouchableOpacity
            style={[styles.button, {backgroundColor:"#FF965C"}]}
            onPress={() => {
              setNoSavingModalVisible(true)
            }}
          >
            <Text style={styles.buttonText}>종료하기</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.button, {backgroundColor:"#FF965C"}]}
            onPress={() => {
              playSound()
              navigation.navigate("SavingMoneyScreen", {accountName:accountName, accountSeq:accountSeq, planSeq:planSeq, totalSavings: totalSavings, currentAccountAmount:currentAccountAmount})
            }}
          >
            <Text style={styles.buttonText}>저축하기</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>

      {/* 미저축 종료 모달창 */}
      { noSavingModalVisible
        ?
        // (<View style={styles.centeredView}>
        (<View style={{}}>
          <Modal
            animationType="none"
            transparent={true}
            visible={noSavingModalVisible}
            onRequestClose={() => {
              setNoSavingModalVisible(false);
            }}>
            <View style={styles.centeredView}>
              <View style={styles.modalView}>
                <Text style={styles.modalText}>저축하지 않고, 종료하시겠습니까?</Text>
                <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                  <TouchableOpacity
                    style={[styles.modalbutton, styles.buttonClose]}
                    onPress={() => {
                      handlePlanNoSaving()
                    }}>
                    <Text style={styles.textStyle}>예</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[styles.modalbutton, styles.buttonClose]}
                    onPress={() => {
                      setNoSavingModalVisible(false)
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
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 15,
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
  contentContainer: {
    flexGrow: 1, // 스크롤 가능하도록 설정
    alignItems: "center",
    paddingTop: 20,
    paddingBottom: 150, // 하단 여백
    marginTop: 40,
  },
  label: {
    fontSize: 24,
    textAlign: "left",
    // fontWeight: "bold",
    marginBottom: 8,
    marginTop: 20,
    color: "#333",
  },
  amount: {
    fontSize: 40,
    textAlign: "left",
    fontWeight: "bold",
    marginBottom: 20,
    color: "#FF965C",
  },
  receipeTable: {
    width: "90%",
    borderWidth: 1,
    borderColor: "#DDD",
    shadowColor: "rgba(0, 0, 0, 0.5)", // 그림자 색상을 어둡게 조정
    shadowOffset: { width: 0, height: 4 }, // 그림자 오프셋을 크게 조정
    shadowOpacity: 1, // 그림자 투명도를 최대로 조정
    shadowRadius: 8, // 그림자 반경을 크게 조정
    backgroundColor: "white",
    // borderTop: 20,
    paddingBottom: 40,
  },
  topContainer: {
    margin: 30,
  },
  tableHeader: {
    flexDirection: "row",
    backgroundColor: "#F5F5F5",
    borderBottomWidth: 1,
    borderColor: "#DDD",
  },
  columnHeader: {
    flex: 1,
    padding: 12,
    fontWeight: "bold",
    textAlign: "center",
  },
  tableRow: {
    flexDirection: "row",
    borderBottomWidth: 0,
    borderColor: "#DDD",
  },
  tableIndexCell: {
    flex: 0.5,
    padding: 12,
    textAlign: "center",
  },
  tableCell: {
    flex: 1,
    padding: 12,
    textAlign: "center",
  },
  tableLine: {
    height: 1,
    backgroundColor: "#000000",
    marginHorizontal: "5%",
  },
  tableDotLine: {
    height: 1,
    borderTopWidth: 1,
    borderColor: "#000000",
    borderStyle: "dashed",
    marginHorizontal: "5%",
  },
  totalCell: {
    fontWeight: "bold",
    backgroundColor: "#F5F5F5",
  },
  button: {
    height: 40,
    width: '40%',
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },

   // 모달 관련 스타일
   centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  modalView: {
    margin: 20,
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
  modalbutton: {
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
