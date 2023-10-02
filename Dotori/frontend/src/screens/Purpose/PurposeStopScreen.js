import React, { useState } from "react";
import { View, StyleSheet, TouchableOpacity, Text, Modal } from "react-native";
import Feather from "react-native-vector-icons/Feather";
import { BarChart } from "react-native-gifted-charts";
import { useDispatch, useSelector } from "react-redux";
import { purposeQuit } from "../../apis/purposeapi"
import { useEffect } from "react";
import { Dimensions } from 'react-native';


export default function PurposeDetailScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const [purposeSeq, setPurposeSeq] = useState(route.params.purposeSeq)
  const [purposeDetailData, setPurposeDetailData] = useState(route.params.purposeDetailData)
  const [isQuitModalVisible, setIsQuitModalVisible] = useState(false)
  const [barData, setbarData] = useState();
  const [spacingWidth, setSpacingWidth] = useState(20)
  const [initialSpacingWidth, setInitialSpacingWidth] = useState(3)


  // 현재 창의 너비를 가져오기
  const windowWidth = Dimensions.get('window').width;

  // 화면의 전체 너비를 가져오기
  const screenWidth = Dimensions.get('screen').width;


  // const sortedPurposeDataList = data.purposeDataList.sort((a, b) => {
  //   const dateA = new Date(a.month);
  //   const dateB = new Date(b.month);
  //   return dateA - dateB;
  // });
  // const barData = generateBarData(sortedPurposeDataList);

  
  const generateBarData = (data) => {
    const barData = [];
    const currentDate = new Date();

    if (data.length === 0) {
      return barData;
    }

    data.forEach((item) => {
      const { month, dataAmount } = item;
      const [ year, monthNumber ] = month.split('-'); // 월과 연도 추출

      let isCurrentYearMonth = false;

      if(currentDate.getFullYear() == year && currentDate.getMonth() + 1 == monthNumber) {
        isCurrentYearMonth = true;
      }

      barData.push({
        value: dataAmount,
        label: (monthNumber != 1) ? parseInt(monthNumber) : `${year.slice(-2)}/${parseInt(monthNumber)}`, // 월 표시 형식 변경
        // frontColor: isCurrentYearMonth ? '#31C68F' : '#F1F1F1',
        frontColor: isCurrentYearMonth ? '#31C68F' : (dataAmount === Math.max(...data.map(item => item.dataAmount)) ? '#ED4343' : '#F1F1F1'),
      });
    });
    if(barData.length <= 7){
      setSpacingWidth(Math.floor(windowWidth * 0.45 / barData.length))
      setInitialSpacingWidth(Math.floor(windowWidth * 0.45 / barData.length)/2)
    }
    if(barData.length <= 6){
    }
    return barData;
  };
  
  const doPurposeQuit = async () => {
    try{
      const response = await purposeQuit(purposeSeq, accessToken, grantType)
      if(response.status === 200){
        console.log('목표 중단 완료')
        navigation.reset({
          index: 0,
          routes: [{ name: 'PurposeScreen' }],
        });
      }else{
        console.log('목표 중단 실패', response.status)
      }
      // console.log("테스트")
    }catch(error){
      console.log('오류 발생: 목표 중단 실패', error)
    }
  }
  
  const settingbarData = () =>{
    // Get the start and end dates from the response
    const startDate = new Date(purposeDetailData.startedAt);
    const endDate = new Date(purposeDetailData.endAt);
  
    // Create a map to group data by month
    const monthlyDataMap = {};
  
    // Loop through the purposeDataList and group data by month
    purposeDetailData.purposeDataList.forEach(item => {
      const dataDate = new Date(item.dataCreatedAt);
      const monthYearKey = `${dataDate.getFullYear()}-${(dataDate.getMonth() + 1).toString().padStart(2, '0')}`;
  
      if (!monthlyDataMap[monthYearKey]) {
        monthlyDataMap[monthYearKey] = 0;
      }
  
      monthlyDataMap[monthYearKey] += item.dataAmount;
    });
    console.log(monthlyDataMap) //{"2023-09": 6, "2023-10": 15, "2023-11": 24, "2023-12": 165}
    
    // Create an array of purposeData objects
    const purposeDataList = [];
  
    // Loop through the months in the date range and create purposeData objects
    let currentDate = new Date(startDate); // startDate 변수를 복사하여 currentDate에 할당

    while (currentDate <= endDate) {
      const monthYearKey = `${currentDate.getFullYear()}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}`;
      const dataAmount = monthlyDataMap[monthYearKey] || 0;
    
      purposeDataList.push({
        month: monthYearKey,
        dataAmount
      });
    
      // Move to the next month by creating a new date object
      currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1); // 다음 달의 1일로 설정
    }
  
    console.log(purposeDataList); //[{"dataAmount": 6, "month": "2023-09"}, {"dataAmount": 15, "month": "2023-10"}, {"dataAmount": 24, "month": "2023-11"}]
    // const barData = generateBarData(purposeDataList);
    setbarData(generateBarData(purposeDataList))
  }

  

  

  useEffect(()=>{
    settingbarData()
  }, [])


  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <View style={{ flex: 1 }} />
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Feather name="x" size={30} style={styles.closeIcon} />
        </TouchableOpacity>
      </View>

      {/* 목표 이름 */}
      <View style={[styles.titleContainer, {}]}>
        {/* <Text style={styles.purposeTitle}>{data.purposeTitle}</Text> */}
        <Text style={styles.purposeTitle}>{purposeDetailData.purposeTitle}</Text>
      </View>

      <View style={{flexDirection:'row', marginVertical:20}}>
        {/* 금액 정보 */}
        <View style={[styles.balanceContainer, {marginVertical:20}]}>
          <View style={styles.balanceItem}>
            <Text style={styles.balanceLabel}>목표 금액</Text>
            <Text style={styles.balanceValue}>
              {/* {data.goalAmount.toLocaleString()}원 */}
              {purposeDetailData.goalAmount.toLocaleString()}원
            </Text>
          </View>
          <View style={styles.balanceItem}>
            <Text style={styles.balanceLabel}>현재 금액</Text>
            <Text style={styles.balanceValue}>
              {/* {data.currentBalance.toLocaleString()}원 */}
              {purposeDetailData.currentBalance.toLocaleString()}원
            </Text>
          </View>
          <View style={styles.balanceItem}>
            <Text style={styles.balanceLabel}>남은 금액</Text>
            <Text style={styles.balanceValue}>
              {/* {(data.goalAmount - data.currentBalance).toLocaleString()}원 */}
              {purposeDetailData.goalAmount - purposeDetailData.currentBalance > 0
                ?(purposeDetailData.goalAmount - purposeDetailData.currentBalance).toLocaleString()
                : 0}원
            </Text>
          </View>
        </View>
      </View>

      {/* 차트 */}
      <View style={styles.chartContainer}>
        <View style={styles.chartInfoContainer}>
          <Text style={styles.chartInfoText}>현재 달성</Text>
          <Text style={styles.chartInfoPercent}>{(purposeDetailData.currentBalance / purposeDetailData.goalAmount * 100).toFixed(2)}%</Text>
        </View>

        <View style={styles.chartDataContainer}>
          <BarChart
              data={barData}
              noOfSections={2}
              isAnimated
              barWidth={15}
              width={250}
              height={150}
              initialSpacing={initialSpacingWidth}
              // spacing={barData.length <= 7 ? Math.floor(windowWidth * 0.5 / barData.length) : 20}
              spacing={spacingWidth}
              hideRules
              hideYAxisText
              barBorderRadius={4}
              xAxisTextStyle={{color: 'gray'}}
              yAxisTextStyle={{color: 'gray'}}
              xAxisThickness={0}
              yAxisThickness={0}
              renderTooltip={(item, index) => {
                return (
                  <View
                    style={{
                      marginBottom: 10,
                      marginLeft: 0,
                      backgroundColor: 'lightgray',
                      paddingHorizontal: 6,
                      paddingVertical: 4,
                      borderRadius: 4,
                    }}>
                    <Text>{item.value.toLocaleString()}</Text>
                  </View>
                );
              }}
            />
        </View>
      </View>
      <View style={{ width: "90%", alignItems: "flex-end", justifyContent: "center", margin: 20, }}>
        <TouchableOpacity
          onPress={()=>{
            setIsQuitModalVisible(true)
          }}
        >
          <Text style={{fontSize: 13, color: "#939393",}}>목표 중단하기</Text>
        </TouchableOpacity>
      </View>

      {/* 중단 확인 문구 */}
      {/* <View style={styles.stopCheckContainer}>
        <Text style={styles.stopCheckText}>목표를 중단하시겠습니까?</Text>
      </View> */}

      {/* 확인 버튼 */}
      {/* <View style={styles.purposeStopContainer}>
        <TouchableOpacity
          style={styles.stopPurposeButtonYes}
          onPress={() => {
            // 중단 요청
            setIsQuitModalVisible(true)
          }}
        >
          <View style={styles.stopPurposeButtonInner}>
            <Text style={styles.stopPurposeText}>네</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.stopPurposeButtonNo}
          onPress={() => { navigation.goBack() }}
        >
          <View style={styles.stopPurposeButtonInner}>
            <Text style={styles.stopPurposeText}>아니오</Text>
          </View>
        </TouchableOpacity>
      </View> */}

      


      {/* 목표 종료 모달 */}
      {/* 회원탈퇴 모달창 */}
      <View style={styles.centeredView}>
        <Modal
          animationType="none"
          transparent={true}
          visible={isQuitModalVisible}
          onRequestClose={() => {
            // Alert.alert('Modal has been closed.');
            setIsQuitModalVisible(false);
          }}>
          <View style={styles.centeredView}>
            <View style={styles.modalView}>
              <Text style={styles.modalText}>
                목표까지 {"\n"}
                {purposeDetailData.goalAmount - purposeDetailData.currentBalance > 0
                ?(purposeDetailData.goalAmount - purposeDetailData.currentBalance).toLocaleString()
                : 0}원 
                남았습니다.{"\n"} 목표 진행을 중단 하시겠습니까?
              </Text>
              <View style={{flexDirection:'row', justifyContent:'space-around'}}>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => {
                    navigation.navigate("PurposeStop1Screen", {purposeSeq:purposeSeq, purposeDetailData:purposeDetailData})
                    setIsQuitModalVisible(false)
                    // doPurposeQuit()
                  }}>
                  <Text style={styles.textStyle}>예</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => {
                    setIsQuitModalVisible(false)
                    // cancelUserWithdrawDotori()
                  }}>
                  <Text style={styles.textStyle}>아니오</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    width: "90%",
    justifyContent: "flex-end",
    marginTop: 20,
    marginBottom: 20,
  },
  titleContainer: {
    width: "100%",
    alignItems: "center",
    marginBottom: 40,
    marginTop: 20,
  },
  purposeTitle: {
    fontSize: 30,
    fontWeight: "bold",
  },
  balanceContainer: {
    width: "100%",
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
    paddingHorizontal: 20,
  },
  balanceItem: {
    alignItems: "center",
  },
  balanceLabel: {
    fontSize: 15,
    marginBottom: 5,
    fontWeight: "bold",
  },
  balanceValue: {
    fontSize: 15,
  },
  chartContainer: {
    width: "90%",
    height: 350,
    borderRadius: 10,
    marginBottom: 20,
    padding: 20,
    borderWidth: 1,
    borderColor: "#EDEDED",
  },
  chartInfoContainer: {
    alignItems: "center",
    marginTop: 15,
    marginBottom: 15,
  },
  chartInfoText: {
    fontSize: 15,
    fontWeight: "bold",
  },
  chartInfoPercent: {
    fontSize: 40,
    fontWeight: "bold",
    color: "#ED4343",
  },
  chartDataContainer: {

  },
  stopCheckContainer: {
    width: "100%",
    alignItems: "center",
    marginBottom: 30,
  },
  stopCheckText: {
    fontSize: 18,
  },
  purposeStopContainer: {
    width: "100%",
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    margin: 20,
  },
  stopPurposeButtonYes: {
    width: "40%",
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 5,
    marginHorizontal: "2.5%",
  },
  stopPurposeButtonNo: {
    width: "40%",
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 5,
    marginHorizontal: "2.5%",
  },
  stopPurposeButtonInner: {
    alignItems: "center",
    justifyContent: "center", // 추가
  },
  stopPurposeText: {
    fontSize: 20,
    color: "white",
    textAlignVertical: 'center', // 추가
    marginVertical: 5
  },
  closeIcon: {
    // marginRight: 20,
  },

  // 목표 종료 모달 스타일
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
    lineHeight: 24,
  },
});
