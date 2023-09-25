import React from "react";
import { View, StyleSheet, TouchableOpacity, Text } from "react-native";
import Feather from "react-native-vector-icons/Feather";
import { BarChart } from "react-native-gifted-charts";
import { useDispatch, useSelector } from "react-redux";

const data = {
  id: "1",
  purposeTitle: "로마여행",
  currentBalance: 2000000,
  goalAmount: 4000000,
  purposeDataList: [
    {
      month: "2022-11",
      dataAmount: 300000,
    },
    {
      month: "2023-01",
      dataAmount: 500000,
    },
    {
      month: "2022-12",
      dataAmount: 350000,
    },
    {
      month: "2023-03",
      dataAmount: 50000,
    },
    {
      month: "2023-02",
      dataAmount: 250000,
    },
    {
      month: "2023-04",
      dataAmount: 420000,
    },
    {
      month: "2023-05",
      dataAmount: 360000,
    },
    {
      month: "2023-06",
      dataAmount: 100000,
    },
    {
      month: "2023-07",
      dataAmount: 450000,
    },
    {
      month: "2023-08",
      dataAmount: 240000,
    },
    {
      month: "2023-09",
      dataAmount: 320000,
    }
  ]
};



const generateBarData = (data) => {
  const barData = [];
  const currentDate = new Date();

  if (data.length === 0) {
    return barData;
  }

  data.forEach((item) => {
    const { month, dataAmount } = item;
    const [year, monthNumber] = month.split('-'); // 월과 연도 추출

    let isCurrentYearMonth = false;

    if(currentDate.getFullYear() == year && currentDate.getMonth() + 1 == monthNumber) {
      isCurrentYearMonth = true;
    }

    barData.push({
      value: dataAmount,
      label: (monthNumber != 1) ? parseInt(monthNumber) : `${year.slice(-2)}/${parseInt(monthNumber)}`, // 월 표시 형식 변경
      frontColor: isCurrentYearMonth ? '#31C68F' : '#F1F1F1',
    });
  });

  return barData;
};

export default function PurposeDetailScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외

const sortedPurposeDataList = data.purposeDataList.sort((a, b) => {
  const dateA = new Date(a.month);
  const dateB = new Date(b.month);
  return dateA - dateB;
});

  const barData = generateBarData(sortedPurposeDataList);

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
      <View style={styles.titleContainer}>
        <Text style={styles.purposeTitle}>{data.purposeTitle}</Text>
      </View>

      {/* 금액 정보 */}
      <View style={styles.balanceContainer}>
        <View style={styles.balanceItem}>
          <Text style={styles.balanceLabel}>목표 금액</Text>
          <Text style={styles.balanceValue}>
            {data.goalAmount.toLocaleString()}원
          </Text>
        </View>
        <View style={styles.balanceItem}>
          <Text style={styles.balanceLabel}>현재 금액</Text>
          <Text style={styles.balanceValue}>
            {data.currentBalance.toLocaleString()}원
          </Text>
        </View>
        <View style={styles.balanceItem}>
          <Text style={styles.balanceLabel}>남은 금액</Text>
          <Text style={styles.balanceValue}>
            {(data.goalAmount - data.currentBalance).toLocaleString()}원
          </Text>
        </View>
      </View>

      {/* 차트 */}
      <View style={styles.chartContainer}>
        <View style={styles.chartInfoContainer}>
          <Text style={styles.chartInfoText}>현재 달성</Text>
          <Text style={styles.chartInfoPercent}>{data.currentBalance / data.goalAmount * 100}%</Text>
        </View>

        <View style={styles.chartDataContainer}>
          <BarChart
              data={barData}
              noOfSections={2}
              isAnimated
              barWidth={15}
              width={250}
              height={150}
              initialSpacing={3}
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

      {/* 중단 확인 문구 */}
      <View style={styles.stopCheckContainer}>
        <Text style={styles.stopCheckText}>목표를 중단하시겠습니까?</Text>
      </View>

      {/* 확인 버튼 */}
      <View style={styles.purposeStopContainer}>
        <TouchableOpacity
          style={styles.stopPurposeButtonYes}
          onPress={() => {
            // 중단 요청
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
    width: "100%",
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
    marginRight: 20,
  },
});
