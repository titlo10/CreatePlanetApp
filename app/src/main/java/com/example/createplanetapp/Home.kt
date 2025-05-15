package com.example.createplanetapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment // Добавлен импорт Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Инициализация RecyclerView
        val excursionsList: RecyclerView = view.findViewById(R.id.rv_featured)

        // Загрузка данных из CSV
        val items = csvParser(requireContext().resources, R.raw.po_gorodu) // Исправлено на requireContext()
        items.addAll(csvParser(requireContext().resources, R.raw.zagorodnie))

        // Настройка адаптера и менеджера
        excursionsList.layoutManager = LinearLayoutManager(requireContext())
        excursionsList.adapter = ExcursionAdapter(items, requireContext()) // Исправлено на ExcursionAdapter

        return view
    }
}